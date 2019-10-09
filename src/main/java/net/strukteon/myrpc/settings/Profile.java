package net.strukteon.myrpc.settings;

/*
    Created by nils on 17.01.2019 at 17:04.
    Project: My Rich Presence
    
    (c) Nils 2019
    strukteon.net
*/

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.strukteon.myrpc.utils.Logger;
import net.strukteon.myrpc.utils.Static;
import sun.rmi.runtime.Log;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Profile {

    private String mrpVersion          = Static.VERSION;

    public String profileName          = null;
    public String appId                = Static.DEFAULT_APP_ID;
    public String details              = "made by strukteon";
    public String state                = "https://strukteon.net";

    public boolean showTimer           = false;
    public boolean isCountingUp        = true;
    public long countdownSeconds       = 0;

    public int curPlayers              = 0;
    public int maxPlayers              = 0;

    public List<String> customImages   = new ArrayList<>();

    public Image largeImage            = new Image();
    public Image smallImage            = new Image();


    public Profile(){
        customImages.addAll(new Application().images);
    }

    public static Profile defaultProfile(){
        Profile p = new Profile();
        p.profileName = "Default";
        return p;
    }



    public static List<Profile> loadProfiles(){
        List<Profile> profiles = new ArrayList<>();
        Gson gson = new Gson();
        for (File f : Static.SETTINGS_FOLDER.listFiles()){
            if (f.getName().endsWith("." + Static.FILE_EXTENSION)){
                Logger.LOG("Profile detected: " + f.getName());
                try {
                    InputStreamReader isr = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8);
                    Profile p = gson.fromJson(isr, Profile.class);
                    isr.close();
                    if (p.profileName == null) {
                        p.profileName = f.getName().substring(0, f.getName().length() - Static.FILE_EXTENSION.length() - 1);
                        Logger.LOG("Trying to update to version: " + Static.VERSION);
                        if (!f.delete())
                            Logger.LOG("Deleting unsuccessful.");
                        else
                            try {
                                p.saveProfile();
                            } catch (IOException e){
                                Logger.LOG("Couldn't save profile: " + e.getMessage());
                            }
                    }
                    profiles.add(p);
                } catch (IOException e) {
                    Logger.LOG("Errored while trying to load profile " + f.getName());
                    e.printStackTrace();
                }
            }
        }
        return profiles;
    }

    public void saveProfile() throws IOException {
        File f = new File(Static.SETTINGS_FOLDER, getFilename());
        saveProfile(f);
    }

    public String getFilename(){
        return profileName.toLowerCase().replaceAll("\\s", "_").replaceAll("[^a-z\\d_\\-]", "") + ".mrpc";
    }

    public void saveProfile(File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        gson.toJson(this, os);
        os.close();
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //return gson.toJson(this);
        return profileName;
    }

    public static class Image {

        public boolean enabled         = false;
        public String imageKey         = "";
        public String imageText        = "";

    }

}
