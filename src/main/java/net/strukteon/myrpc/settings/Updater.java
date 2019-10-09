package net.strukteon.myrpc.settings;

/*
    Created by nils on 06.02.2019 at 00:44.
    Project: My Rich Presence
    
    (c) Nils 2019
    strukteon.net
*/

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.strukteon.myrpc.Main;
import net.strukteon.myrpc.utils.*;
import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

public class Updater {
    private Stage parent;

    public Updater(Stage parent) throws IOException {
        this.parent = parent;


    }

    public static void open(Stage parent, String version) throws IOException {
        Stage dialog = new Stage();
        dialog.setTitle("Updater");
        AnchorPane content =  FXMLLoader.load(Main.class.getResource("/update_layout.fxml"));
        Scene scene = new Scene(content, 300, 100);

        dialog.getIcons().add(new Image(Tools.class.getResourceAsStream("/icon.png")));
        scene.getStylesheets().add("style/default/discord_style/updater.css");
        scene.getStylesheets().addAll(Static.CSS_FILES);

        ProgressBar pbar = (ProgressBar) content.lookup("#pbar");
        Label lb = (Label) content.lookup("#lb");

        new Thread(() -> {
            try {
                String newestVersion = version;
                pbar.setProgress(1);

                Logger.LOG("Your version: %s", Static.VERSION);
                Logger.LOG("Newest available version: %s", newestVersion);
                Logger.LOG(newestVersion == null ? "You are up to date" : "Update available");

                if (newestVersion != null) {
                    pbar.setProgress(0);
                    lb.setText("Downloading...");
                    File dest = new File(String.format("MyRichPresence-%s.jar", newestVersion));
                    Download download = new Download(new URL(Static.DOWNLOAD_URL + String.format("MyRichPresence-%s.jar", newestVersion)), dest);

                    while (download.getStatus() == Download.DOWNLOADING) {
                        Platform.runLater(() -> pbar.setProgress(download.getProgress() / 100));
                    }

                    if (download.getStatus() == Download.COMPLETE)
                        Platform.runLater(() -> pbar.setProgress(1));

                    String separator = System.getProperty("file.separator");
                    String path = "\"" + System.getProperty("java.home")
                            + separator + "bin" + separator + "java" + "\"";
                    String programPath = Updater.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                    if (!SystemUtils.IS_OS_WINDOWS)
                        path = "/usr/bin/java";

                    new ProcessBuilder(path, "-jar", dest.getAbsolutePath(), "delete " + programPath).start();

                    System.exit(0);
                } else {
                    Platform.runLater(() -> lb.setText("You have the newest version!"));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }).start();

        dialog.setScene(scene);
        if (parent != null) {
            dialog.initOwner(parent);
            dialog.initModality(Modality.APPLICATION_MODAL);
        }
        dialog.showAndWait();
    }

    public static String updateAvailable() throws IOException {
        try {
            URL versionURL = new URL(Static.VERSION_URL);
            HttpURLConnection versionConnection = (HttpURLConnection) versionURL.openConnection();
            String newestVersion = readInputStream(versionConnection.getInputStream());
            versionConnection.disconnect();
            return Tools.compareVersions(Static.VERSION, newestVersion) >= 0 ? null : newestVersion;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void checkForUpdates(Stage parent, boolean openDialog) throws IOException {
        String version = updateAvailable();
        if (version == null) {
            if (openDialog)
                open(parent, null);
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().addAll(Static.CSS_FILES);
        alert.getDialogPane().getStylesheets().add("style/default/discord_style/dialog.css");
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Tools.class.getResourceAsStream("/icon.png")));
        alert.setTitle("Updater");
        alert.setHeaderText("Update available");
        alert.setContentText("A new update is available, do want to download it?");

        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            open(parent, version);
        }
    }

    private static String readInputStream(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(is));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }
}
