package net.strukteon.myrpc.utils;

/*
    Created by nils on 17.01.2019 at 17:35.
    Project: My Rich Presence
    
    (c) Nils 2019
    strukteon.net
*/

import javafx.scene.control.TextArea;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static TextArea gui_log;

    public static void LOG(String text, Object... args){
        String parsed = String.format("[%s] %s", getTime(), String.format(text, args));
        if (gui_log != null)
            gui_log.setText(gui_log.getText() + "\n" + parsed);
        System.out.println(parsed);
    }

    public static void WSLOG(String text, Object... args){
        String parsed = String.format("[WS@%s] %s", getTime(), String.format(text, args));
        if (gui_log != null)
            gui_log.setText(gui_log.getText() + "\n" + parsed);
        System.out.println(parsed);
    }

    private static String getTime(){
        return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
    }

    public static void initializeTextArea(TextArea textArea){
        gui_log = textArea;
        gui_log.setText(String.format("MyRichPresence v%s%n", Static.VERSION));
    }

}
