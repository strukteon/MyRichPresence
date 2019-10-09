package net.strukteon.myrpc.startup;

/*
    Created by nils on 27.01.2019 at 00:15.
    Project: My Rich Presence
    
    (c) Nils 2019
    strukteon.net
*/

import net.strukteon.myrpc.Main;
import net.strukteon.myrpc.utils.Static;
import net.strukteon.myrpc.settings.Settings;
import net.strukteon.myrpc.utils.Download;
import net.strukteon.myrpc.utils.UnzipFile;
import org.apache.commons.lang3.SystemUtils;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

public class StartupManager {

    private static JFrame frame;
    private static JPanel container;
    private static JLabel label;
    private static JProgressBar progressBar;
    private static JButton cancelButton;

    public static void main(String[] args) {
        if (!Settings.initFolder()){
            JOptionPane.showMessageDialog(frame, "The Program has no writing permissions in this directory, please move it to another directory or start it with admin privileges.");
            return;
        }

        DeleteOld.checkArgs(args);

        boolean requiresDownload = true;
        try {
            int javaVersion = Integer.parseInt(SystemUtils.JAVA_VERSION.split("\\.")[0]);
            if (javaVersion >= 11) {
                requiresDownload = true;
                System.out.println("JAVA_VERSION >= 11");
            }
            else {
                requiresDownload = false;
                System.out.println("JAVA_VERSION < 11");
            }
        } catch (NumberFormatException e) { }

        if (!requiresDownload || args.length >= 1 && args[0].equals("javafx-installed")){
            System.out.println("Startup complete");

            Main.main(null);

            return;
        }

        File javafxSdk = new File(Static.SETTINGS_FOLDER, "." + Static.JFX_SDK_FOLDER);
        if (javafxSdk.exists()) {
            try {
                restart(javafxSdk.getAbsolutePath());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        frame = new JFrame();
        frame.setTitle("My Rich Presence Startup-Manager");
        frame.setIconImage(new ImageIcon(StartupManager.class.getResource("/icon.png")).getImage());
        frame.setSize(400,150);

        container       = new JPanel();
        label           = new JLabel("Placeholder");
        progressBar     = new JProgressBar(0, 100);
        cancelButton    = new JButton("Cancel");

        cancelButton.setEnabled(false);

        GridBagLayout parentLayout  = new GridBagLayout();
        GroupLayout layout          = new GroupLayout(container);

        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(progressBar)
                .addComponent(cancelButton)
        );

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(label)
                .addComponent(progressBar)
                .addComponent(cancelButton)
        );

        progressBar.setMinimumSize(new Dimension(300, 10));
        container.setLayout(layout);

        frame.add(container);
        frame.setLocationRelativeTo(null);
        frame.setLayout(parentLayout);
        frame.setVisible(true);

        label.setText("Determining the OS...");

        String downloadUrl;

        if (SystemUtils.IS_OS_WINDOWS)      downloadUrl = String.format("https://gluonhq.com/download/javafx-%s-sdk-windows/", Static.JFX_VERSION.replace('.', '-'));
        else if (SystemUtils.IS_OS_MAC)     downloadUrl = String.format("https://gluonhq.com/download/javafx-%s-sdk-mac/", Static.JFX_VERSION.replace('.', '-'));
        else if (SystemUtils.IS_OS_LINUX)   downloadUrl = String.format("https://gluonhq.com/download/javafx-%s-sdk-linux/", Static.JFX_VERSION.replace('.', '-'));
        else {
            label.setText("Sorry, but it seems like your operating system is not supported");
            progressBar.setValue(100);
            return;
        }

        label.setText("Downloading the proper JavaFX SDK...");
        try {
            File temp = new File(Static.SETTINGS_FOLDER, ".jfx.zip");
            if (temp.exists())
                temp.delete();
            temp.createNewFile();
            if (SystemUtils.IS_OS_WINDOWS)
                Runtime.getRuntime().exec(String.format("attrib +H %s/.jfx.zip", Static.SETTINGS_FOLDER.getPath()));
            
            Download download = new Download(new URL(downloadUrl), temp);

            while (download.getStatus() == Download.DOWNLOADING) {
                progressBar.setValue((int) download.getProgress());
                Thread.sleep(10);
            }

            if (download.getStatus() == Download.COMPLETE)
                progressBar.setValue(100);
            else {
                label.setText("An error occurred while downloading the JavaFX SDK");
                return;
            }

            label.setText("Unzipping the SDK...");
            progressBar.setValue(0);

            UnzipFile.unzip(temp, Static.SETTINGS_FOLDER);

            File tempJavafxSdk = new File(Static.SETTINGS_FOLDER, Static.JFX_SDK_FOLDER);
            tempJavafxSdk.renameTo(javafxSdk);
            tempJavafxSdk.delete();

            if (SystemUtils.IS_OS_WINDOWS)
                Runtime.getRuntime().exec(String.format("attrib +H %s/%s", Static.SETTINGS_FOLDER.getPath(), javafxSdk.getName()));

            temp.delete();

            label.setText("Done, enjoy the program!");
            progressBar.setValue(100);

            frame.dispose();

            restart(javafxSdk.getAbsolutePath());

        } catch (IOException | InterruptedException/**/ e) {
            e.printStackTrace();
        }
    }

    private static void restart(String modulePath) throws IOException, InterruptedException {
        System.out.println("test1");
        String separator = System.getProperty("file.separator");
        String path = "\"" + System.getProperty("java.home")
                + separator + "bin" + separator + "java" + "\"";
        if (!SystemUtils.IS_OS_WINDOWS)
            path = "/usr/bin/java";
        String programPath = StartupManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (programPath.startsWith("/") && SystemUtils.IS_OS_WINDOWS) programPath = programPath.substring(1);
        String[] args = new String[]{path, "--module-path", (SystemUtils.IS_OS_WINDOWS ? "\"" : "") + modulePath.replace('\\', '/') + "/lib" + (SystemUtils.IS_OS_WINDOWS ? "\"" : ""),
                    "--add-modules", "javafx.controls,javafx.fxml,javafx.web", "-jar", programPath, "javafx-installed"};
        System.out.println(Arrays.toString(args));
        Process p = new ProcessBuilder(args).start();

        BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        BufferedReader in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        String line2 = null;
        while ((line = in.readLine()) != null || (line2 = in2.readLine()) != null) {
            System.out.println((line2 != null ? line2 : "") + (line != null ? line : ""));
        }

        p.waitFor();
        System.out.println("test2");
    }

}
