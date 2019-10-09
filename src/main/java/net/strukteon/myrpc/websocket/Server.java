package net.strukteon.myrpc.websocket;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.scene.control.Alert;
import net.strukteon.myrpc.control.MyRichPresence;
import net.strukteon.myrpc.utils.Logger;
import net.strukteon.myrpc.utils.Tools;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Server extends WebSocketServer {

    private static Server instance;
    private static Thread instanceThread;
    private MyRichPresence parent;
    private SocketMessage.Service currentService = SocketMessage.Service.END;
    private SocketMessage previousMessage = null;

    public Server(InetSocketAddress address, MyRichPresence parent) {
        super(address);
        this.parent = parent;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Welcome to the server!"); //This method sends a message to the new client
        broadcast( "new connection: " + handshake.getResourceDescriptor() ); //This method sends a message to all clients connected
        Logger.WSLOG("new connection to " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Logger.WSLOG("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
        if (parent.getInterface().presenceMode == 1) {
            parent.handleExtensionPresence(new SocketMessage());
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Logger.WSLOG("received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
        JsonObject o = new GsonBuilder().create().fromJson(message, JsonObject.class);
        SocketMessage socketMessage = new GsonBuilder().create().fromJson(o.get("presence"), SocketMessage.class);
        if (parent.getInterface().presenceMode == 1) {
            if (socketMessage.service == SocketMessage.Service.END && parent.isRunning() || ! currentService.getKey().equals(socketMessage.service.getKey())) {
                parent.stopPresence();
                currentService = socketMessage.service;
            } if (socketMessage.service != SocketMessage.Service.END) {
                Logger.WSLOG("handling socket message");
                if (!socketMessage.equals(previousMessage))
                    parent.handleExtensionPresence(socketMessage);
            }
            previousMessage = socketMessage;
        }

    }

    @Override
    public void onMessage( WebSocket conn, ByteBuffer message ) {
        Logger.WSLOG("received ByteBuffer from "	+ conn.getRemoteSocketAddress());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        Logger.WSLOG("an error occured on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }

    @Override
    public void onStart() {
        Logger.WSLOG("server started successfully");
    }

    public static Server getInstance() {
        return instance;
    }

    public static boolean startServer(int port, MyRichPresence parent){
        if (!available(port)){
            Alert alert = Tools.generateAlert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Unable to connect to the extension");
            alert.setContentText("Maybe another My Rich Presence Instance is already running?");
            alert.show();
            return false;
        }
        if (instance == null){
            instance = new Server(new InetSocketAddress("localhost", port), parent);
            instanceThread = new Thread(() -> {
                instance.run();
            });
            instanceThread.start();
            return true;
        }
        return false;
    }

    private static boolean available(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }
}