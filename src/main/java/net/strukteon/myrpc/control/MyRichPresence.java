package net.strukteon.myrpc.control;

/*
    Created by nils on 17.01.2019 at 16:56.
    Project: My Rich Presence
    
    (c) Nils 2019
    strukteon.net
*/

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.strukteon.myrpc.settings.Application;
import net.strukteon.myrpc.utils.Logger;
import net.strukteon.myrpc.utils.Static;
import net.strukteon.myrpc.settings.Profile;
import net.strukteon.myrpc.settings.Settings;
import net.strukteon.myrpc.utils.SystemTrayManager;
import net.strukteon.myrpc.utils.Tools;
import net.strukteon.myrpc.websocket.Server;
import net.strukteon.myrpc.websocket.SocketMessage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class MyRichPresence {

    public Settings settings;
    private List<Profile> profiles;

    private DiscordRPC lib = DiscordRPC.INSTANCE;
    private DiscordRichPresence presence = new DiscordRichPresence();

    public Stage stage;
    private Interface gui;
    public SystemTrayManager systemTrayManager;

    int currentProfile = 0;
    boolean running = false;

    public MyRichPresence(Stage stage, Node content) throws IOException {
        this.stage = stage;
        systemTrayManager = new SystemTrayManager(stage, this);
        if (!Static.SETTINGS_FOLDER.exists())
            Static.SETTINGS_FOLDER.mkdir();

        Logger.LOG("Loading settings");
        settings = Settings.loadSettings();

        if (Tools.compareVersions(settings.mrpVersion, Static.VERSION) < 0){
            settings.mrpVersion = Static.VERSION;
            settings.saveSettings();
        }
        gui = new Interface(content,this);

        gui.ch_minimize_to_taskbar.setSelected(settings.minimizeToTaskbar);
        gui.ch_resizable.setSelected(settings.isResizable);

        Logger.LOG("Loading profiles");
        profiles = Profile.loadProfiles();
        gui.cb_profiles.getItems().addAll(getProfileNames());
        Logger.LOG("%d Profiles loaded", profiles.size());

        if (profiles.size() == 0){
            Profile p = Profile.defaultProfile();
            profiles.add(p);
            p.saveProfile();
            gui.cb_profiles.getItems().add(p.profileName);
            Logger.LOG("Created the default profile");
            gui.loadProfile(p);
        }
        gui.cb_profiles.getSelectionModel().selectFirst();
        gui.bt_remove_profile.setDisable(profiles.size() == 1);

        Server.startServer(Static.WS_PORT, this);
    }

    public void createProfile(String name) throws IOException {
        Profile p = new Profile();
        p.profileName = name;
        p.saveProfile();
        profiles.add(p);
        gui.cb_profiles.getItems().add(name);
        Logger.LOG("Successfully created the profile \"%s\"", name);
    }

    public void removeProfile(int index){
        boolean selectRequired = gui.cb_profiles.getSelectionModel().getSelectedIndex() == index;
        String profileName = profiles.get(index).profileName;
        File profileFile = new File(Static.SETTINGS_FOLDER, profiles.get(index).getFilename());
        if (profileFile.delete()) {
            gui.bt_remove_profile.setDisable(profiles.size() == 1);
            profiles.remove(index);
            System.out.println(profiles);
            gui.cb_profiles.getItems().remove(index);
            if (selectRequired)
                gui.cb_profiles.getSelectionModel().selectFirst();
            Logger.LOG("Removed the profile at position %d (%s)", index, profileName);
        } else
            Logger.LOG("An error occurred while trying to delete profile %s", profileName);
    }


    /*public void createApplication(Application application) {  // TODO
        gui.cb_application.getItems().add(application.name);
        settings.applications.add(application);
        settings.saveSettings();
        Logger.LOG("Successfully added the application \"%s\"", application.name);
    }

    /*public void removeApplication(int index){ // TODO
        boolean selectRequired = gui.cb_application.getSelectionModel().getSelectedIndex() == index;
        String id = settings.applications.get(0).id;
        Boolean replace = null;
        for (Profile p : profiles)
            if (p.appId.equals(id)) {
                if (replace == null) {
                   // Tools.
                }
            }
    }*/


    public void setIconified(boolean iconified) {
        stage.setIconified(iconified);
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public List<String> getProfileNames(){
        return profiles.stream().map(p -> p.profileName).collect(Collectors.toList());
    }

    public Profile getCurrentProfile() {
        return profiles.get(currentProfile);
    }

    public void updatePresence(){
        Profile p = getCurrentProfile();
        if (!running)
            lib.Discord_Initialize(p.appId, null, false, "");
        presence = new DiscordRichPresence();
        presence.details = p.details;
        presence.state = p.state;
        presence.partySize = p.curPlayers;
        presence.partyMax = p.maxPlayers;

        if (p.largeImage.enabled) {
            presence.largeImageKey = p.largeImage.imageKey;
            presence.largeImageText = p.largeImage.imageText;
        }

        if (p.smallImage.enabled) {
            presence.smallImageKey = p.smallImage.imageKey;
            presence.smallImageText = p.smallImage.imageText;
        }

        if (p.showTimer){
            if (p.isCountingUp) {
                presence.startTimestamp = System.currentTimeMillis();
            } else {
                presence.endTimestamp = System.currentTimeMillis() + p.countdownSeconds * 1000;
            }
        }

        lib.Discord_UpdatePresence(presence);
        running = true;

        Logger.LOG("Updated the presence");
    }

    public void handleExtensionPresence(SocketMessage socketMessage){
        if (!running)
            lib.Discord_Initialize(socketMessage.service.getAppId(), null, false, "");
        presence = new DiscordRichPresence();
        presence.largeImageKey = socketMessage.service.getImageKey();
        presence.largeImageText = "Made with MyRPC";
        presence.smallImageText = "download from strukteon.net";

        if (socketMessage.state == SocketMessage.State.NONE) {
            presence.details = "Browsing";
            presence.startTimestamp = System.currentTimeMillis();
        } else {
            presence.details = socketMessage.title;
            if (socketMessage.author != null)
                presence.state = "by " + socketMessage.author;
            if (socketMessage.state == SocketMessage.State.PLAYING)
                presence.endTimestamp = System.currentTimeMillis() + socketMessage.remaining * 1000;
            presence.smallImageKey = socketMessage.state.getKey();
        }
        running = true;
        lib.Discord_UpdatePresence(presence);
        Logger.LOG("Updated the web extension presence");
    }

    public void stopPresence() {
        if (running) {
            lib.Discord_ClearPresence();
            lib.Discord_Shutdown();
            gui.bt_update_presence.setText("Start Presence");
            running = false;
            Logger.LOG("Stopped the presence");
        }
    }

    public void storeProfile(){
        Profile p = getCurrentProfile();
        p.details               = gui.tf_details.getText();
        p.state                 = gui.tf_state.getText();
        p.showTimer             = gui.ch_show_timer.isSelected();
        p.curPlayers            = Integer.parseInt(gui.tf_group_cur.getText());
        p.maxPlayers            = Integer.parseInt(gui.tf_group_max.getText());
        p.appId                 = gui.tf_app_id.getText();

        p.largeImage.enabled    = gui.ch_large_img_enabled.isSelected();
        p.largeImage.imageKey   = gui.cb_large_image.getValue().toLowerCase();
        p.largeImage.imageText  = gui.tf_large_image.getText();

        p.smallImage.enabled    = gui.ch_small_img_enabled.isSelected();
        p.smallImage.imageKey   = gui.cb_small_image.getValue().toLowerCase();
        p.smallImage.imageText  = gui.tf_small_image.getText();
    }

    public void storeSettings(){
        settings.minimizeToTaskbar = gui.ch_minimize_to_taskbar.isSelected();
        settings.isResizable = gui.ch_resizable.isSelected();

        settings.saveSettings();
    }


    public boolean isRunning() {
        return running;
    }

    public Interface getInterface() {
        return gui;
    }

    public static boolean openWebpage(String url) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URL(url).toURI());
                Logger.LOG("Opened the url \"%s\"", url);
                return true;
            } catch (Exception e) {
                Logger.LOG("An error ocurred while trying to open the following URL: %s", url);
                e.printStackTrace();
            }
        }
        return false;
    }

}
