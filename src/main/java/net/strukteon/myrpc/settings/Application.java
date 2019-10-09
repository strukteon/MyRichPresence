package net.strukteon.myrpc.settings;

/*
    Created by nils on 07.04.2019 at 00:38.
    Project: My Rich Presence
    
    (c) Nils 2019
    strukteon.net
*/

import net.strukteon.myrpc.utils.Static;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application {
    public String name;
    public String id;
    public List<String> images = new ArrayList<>();

    public Application(){
        name = "Default Application";
        id = Static.DEFAULT_APP_ID;
        images.addAll(Arrays.asList(
                "superhot",
                "pubg",
                "fortnite",
                "overwatch",
                "chip",
                "csgo",
                "magnet",
                "smile",
                "myrpc",
                "computer",
                "xbox",
                "playstation",
                "nintendo"));
    }
}
