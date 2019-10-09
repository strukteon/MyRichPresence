package net.strukteon.myrpc.websocket;

/*
    Created by nils on 13.03.2019 at 11:08.
    Project: My Rich Presence
    
    (c) Nils 2019
    strukteon.net
*/

import com.google.gson.GsonBuilder;
import net.strukteon.myrpc.utils.Static;

public class SocketMessage {

    public Service service = Service.END;
    public State state;
    public String title;
    public String author;
    public int remaining;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SocketMessage)
            return ((SocketMessage) obj).service == service &&
                    ((SocketMessage) obj).state == state &&
                    ((SocketMessage) obj).title.equals(title) &&
                    ((SocketMessage) obj).author.equals(author) &&
                    ((SocketMessage) obj).remaining == remaining;
        return false;
    }

    public enum Service {
        END("end", null, null),
        YOUTUBE("youtube", "557619972738777102", "logo");

        String key;
        String imageKey;
        String appId;

        Service(String key, String appId, String imageKey){
            this.key = key;
            this.appId = appId;
            this.imageKey = imageKey;
        }

        public String getKey() {
            return key;
        }

        public String getAppId() {
            return appId;
        }

        public String getImageKey() {
            return imageKey;
        }
    }

    public String toJSON() {
        return new GsonBuilder().create().toJson(this);
    }

    public SocketMessage fromJSON(String json){
        return new GsonBuilder().create().fromJson(json, SocketMessage.class);
    }

    public enum State {
        PLAYING("play"),
        PAUSED("pause"),
        NONE(null);

        String key;

        State(String key){
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

}
