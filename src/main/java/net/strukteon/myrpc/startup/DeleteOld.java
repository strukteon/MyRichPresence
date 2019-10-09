package net.strukteon.myrpc.startup;

/*
    Created by nils on 10.02.2019 at 17:18.
    Project: My Rich Presence
    
    (c) Nils 2019
    strukteon.net
*/

import java.io.File;

public class DeleteOld {

    public static void checkArgs(String[] args){
        for (String s : args){
            if (s.startsWith("delete ")){
                File oldVersion = new File(s.replaceFirst("delete ", ""));
                new Thread(() -> {
                    while (oldVersion.exists()){
                        if (!oldVersion.delete()) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        }
    }

}
