package net.strukteon.myrpc.portable;

/*
    Created by nils on 09.04.2019 at 22:35.
    Project: My Rich Presence
    
    (c) Nils 2019
    strukteon.net
*/

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.strukteon.myrpc.settings.Profile;
import net.strukteon.myrpc.utils.Static;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Information {
    public String title                = "Krasser titel yo";
    public String author               = "Itsa me, mario";

    public String appId                = "sadasdsad";
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

    public class Image {
        public boolean enabled         = false;
        public String imageKey         = "";
        public String imageText        = "";
    }

    public static Information loadInformation(){
        return new Gson().fromJson(new InputStreamReader(Information.class.getResourceAsStream("/information.json")), Information.class);
    }

}
