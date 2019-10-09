package net.strukteon.myrpc.portable;

/*
    Created by nils on 16.06.2019 at 00:47.
    Project: My Rich Presence
    
    (c) Nils 2019
    strukteon.net
*/

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class PresenceManager {

    private DiscordRPC lib = DiscordRPC.INSTANCE;
    private DiscordRichPresence presence = new DiscordRichPresence();

    private Information information;

    public void setInformation(Information information) {
        
    }

    public void start(){

    }

}
