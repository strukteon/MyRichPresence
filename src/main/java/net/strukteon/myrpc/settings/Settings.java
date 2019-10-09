package net.strukteon.myrpc.settings;

/*
    Created by nils on 17.01.2019 at 17:45.
    Project: My Rich Presence
    
    (c) Nils 2019
    strukteon.net
*/

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.strukteon.myrpc.utils.Logger;
import net.strukteon.myrpc.utils.Static;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Settings {
    private static final File SETTINGS_FILE = new File(Static.SETTINGS_FOLDER, "settings.json");

    public String mrpVersion            = Static.VERSION;

    public boolean minimizeToTaskbar    = false;
    public boolean isResizable          = false;

    public double windowHeight = 0, windowWidth = 0;

    public List<Application> applications = new ArrayList<>(Collections.singletonList(new Application()));

    public static boolean initFolder(){
        if (!Static.SETTINGS_FOLDER.exists())
            return Static.SETTINGS_FOLDER.mkdirs();
        return true;
    }

    public void saveSettings() {
        mrpVersion = Static.VERSION;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(SETTINGS_FILE), StandardCharsets.UTF_8);
            gson.toJson(this, os);
            os.close();
        } catch (IOException e) {
            Logger.LOG("An error occurred while trying to save settings");
        }
    }

    public static Settings loadSettings() throws IOException {
        if (!SETTINGS_FILE.exists()){
            Logger.LOG("Settings file does not exist, and will be therefore created");
            if (SETTINGS_FILE.createNewFile()){
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(SETTINGS_FILE), StandardCharsets.UTF_8);
                gson.toJson(new Settings(), os);
                os.close();
                Logger.LOG("Settings file was successfully created");
            } else {
                Logger.LOG("Error: Could not create the settings file");
                Logger.LOG("You will not be able to save any settings changed.");
            }
            return new Settings();
        }

        Settings settings = new Gson().fromJson(new FileReader(SETTINGS_FILE), Settings.class);
        Logger.LOG("Settings successfully loaded");
        return settings;
    }

}
