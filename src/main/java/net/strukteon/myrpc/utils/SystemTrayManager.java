package net.strukteon.myrpc.utils;

/*
    Created by nils on 31.01.2019 at 19:13.
    Project: My Rich Presence
    
    (c) Nils 2019
    strukteon.net
*/

import javafx.application.Platform;
import javafx.stage.Stage;
import net.strukteon.myrpc.Main;
import net.strukteon.myrpc.control.MyRichPresence;
import net.strukteon.myrpc.settings.Profile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class SystemTrayManager {

    private SystemTray tray;
    private Stage stage;
    private MyRichPresence myRichPresence;


    private PopupMenu popup;
    private TrayIcon trayIcon;

    private MenuItem openItem   = new MenuItem("Open");
    private Menu selectMenu     = new Menu("Select profile");
    private MenuItem stopItem   = new MenuItem("Stop Presence");
    private MenuItem exitItem   = new MenuItem("Exit");

    private MenuItem selectedProfile = null;

    private Image activeTrayIcon;
    private Image inactiveTrayIcon;

    public SystemTrayManager(Stage stage, MyRichPresence myRichPresence) throws IOException {
        if (!SystemTray.isSupported()){
            Logger.LOG("It seems like this device doesn't support system tray");
            return;
        }

        this.stage = stage;
        this.tray = SystemTray.getSystemTray();
        this.myRichPresence = myRichPresence;

        activeTrayIcon = ImageIO.read(SystemTrayManager.class.getResourceAsStream("/icon_active_16px.png"));
        inactiveTrayIcon = ImageIO.read(SystemTrayManager.class.getResourceAsStream("/icon_inactive_16px.png"));

        popup = new PopupMenu();
        trayIcon = new TrayIcon(myRichPresence.isRunning() ? activeTrayIcon : inactiveTrayIcon);
        tray = SystemTray.getSystemTray();

        if (!myRichPresence.isRunning())
            stopItem.setLabel("Start Presence");

        openItem.addActionListener(e -> {
            Platform.runLater(() -> stage.show());
            System.out.println("ok");
            hide();
        });

        trayIcon.addActionListener(e -> {
            Platform.runLater(stage::show);
            hide();
        });

        exitItem.addActionListener(e -> {
            hide();
            System.exit(0);
        });

        stopItem.addActionListener(e -> {
            trayIcon.setImage(!myRichPresence.isRunning() ? activeTrayIcon : inactiveTrayIcon);
            if (myRichPresence.isRunning()){
                stopItem.setLabel("Start Presence");
                myRichPresence.stopPresence();
            } else {
                stopItem.setLabel("Stop Presence");
                Platform.runLater(() -> myRichPresence.getInterface().bt_update_presence.fire());
            }
        });

        //Add components to pop-up menu
        popup.add(openItem);
        popup.addSeparator();
        popup.add(selectMenu);
        popup.addSeparator();
        popup.add(stopItem);
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);
        trayIcon.setToolTip(String.format("My Rich Presence v%s", Static.VERSION));
    }

    public void show(){
        List<Profile> profiles = myRichPresence.getProfiles();
        selectMenu.removeAll();
        trayIcon.setImage(myRichPresence.isRunning() ? activeTrayIcon : inactiveTrayIcon);
        for (int i = 0; i < profiles.size(); i++){
            Profile p = profiles.get(i);
            MenuItem mi = new MenuItem(p.profileName);
            if (myRichPresence.getCurrentProfile() == p) {
                mi.setEnabled(false);
                selectedProfile = mi;
            }
            int finalI = i;
            mi.addActionListener(e -> {selectedProfile.setEnabled(true);
                mi.setEnabled(false);
                selectedProfile = mi;
                Platform.runLater(() -> {
                    myRichPresence.getInterface().cb_profiles.getSelectionModel().select(finalI);
                });
            });

            selectMenu.add(mi);
        }

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }

    public void hide(){
        tray.remove(trayIcon);

    }
}
