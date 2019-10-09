package net.strukteon.myrpc.control;

/*
    Created by nils on 29.12.2018 at 20:42.
    Project: My Rich Presence
    
    (c) Nils 2018
    strukteon.net
*/

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import net.strukteon.myrpc.settings.Settings;
import net.strukteon.myrpc.utils.SystemTrayManager;

import java.util.Set;

public class ToolbarItems extends HBox {

    private double xOffset;
    private double yOffset;

    private Button minimizeBtn  = new Button("-");
    private Button closeBtn     = new Button("x");

    private Settings settings;
    public SystemTrayManager systemTrayManager;

    public ToolbarItems(Stage stage) {
        this.getStyleClass().add("toolbar-base");
        Label title = new Label("MY RICH PRESENCE");
        Region spacer = new Region();

        closeBtn.setOnMouseClicked(event -> {
            if (!settings.minimizeToTaskbar) {
                stage.setIconified(true);
                System.exit(0);
            } else {
                System.out.println("hiding");
                stage.hide();
                systemTrayManager.show();
            }
        });

        minimizeBtn.setOnMouseClicked(event -> {
            stage.setIconified(true);
        });

        setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });

        title.getStyleClass().add("title");
        minimizeBtn.getStyleClass().add("minimize-button");
        closeBtn.getStyleClass().add("close-button");

        setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinWidth(Region.USE_PREF_SIZE);
        setAlignment(Pos.CENTER);
        getStyleClass().add("toolbar");
        this.getChildren().addAll(title, spacer, minimizeBtn, closeBtn);
    }

    public void setSettings(Settings settings, SystemTrayManager manager){
        this.settings = settings;
        this.systemTrayManager = manager;
    }

    public void updateButtons(){
        minimizeBtn.setMinWidth(minimizeBtn.getHeight());
        closeBtn.setMinWidth(minimizeBtn.getHeight());
    }
}
