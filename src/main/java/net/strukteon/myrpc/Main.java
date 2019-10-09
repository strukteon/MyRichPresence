package net.strukteon.myrpc;

/*
    Created by nils on 29.12.2018 at 19:49.
    Project: My Rich Presence
    
    (c) Nils 2019
    strukteon.net
*/


import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.strukteon.myrpc.control.MyRichPresence;
import net.strukteon.myrpc.control.ToolbarItems;
import net.strukteon.myrpc.settings.Updater;
import net.strukteon.myrpc.utils.Static;

public class Main extends Application {

    public static void main(String[] args) {
        System.out.printf("Trying to start program with Java %s%n%n", System.getProperty("java.version"));
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setMinHeight(610);
        stage.setMinWidth(300);
        Platform.setImplicitExit(false);
        stage.setTitle("My Rich Presence");
        VBox container = new VBox();
        Scene scene = new Scene(container, 540, 770);
        ToolbarItems toolBar = new ToolbarItems(stage);
        StackPane content = FXMLLoader.load(Main.class.getResource("/MainFXML.fxml"));
        MyRichPresence myRichPresence = new MyRichPresence(stage, content);
        toolBar.setSettings(myRichPresence.settings, myRichPresence.systemTrayManager);

        addCopyright(content);

        stage.setOnCloseRequest(e -> {
            if (!myRichPresence.settings.minimizeToTaskbar) {
                stage.setIconified(true);
                System.exit(0);
            } else {
                System.out.println("hiding");
                stage.hide();
                toolBar.systemTrayManager.show();
            }
        });

        if (!myRichPresence.settings.isResizable){
            container.getChildren().add(toolBar);

            container.setPadding(new Insets(20,20,20,20));
            container.setEffect(new DropShadow());
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add("style/default/outer-shadow.css");

            stage.initStyle(StageStyle.TRANSPARENT);
        } else {
            if (myRichPresence.settings.windowHeight > 0)
                stage.setHeight(myRichPresence.settings.windowHeight);
            if (myRichPresence.settings.windowWidth > 0)
                stage.setWidth(myRichPresence.settings.windowWidth);
            ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
                myRichPresence.settings.windowHeight = stage.getHeight();
                myRichPresence.settings.windowWidth = stage.getWidth();
                myRichPresence.settings.saveSettings();
            };

            stage.widthProperty().addListener(stageSizeListener);
            stage.heightProperty().addListener(stageSizeListener);
        }

        content.getStyleClass().add("content");
        VBox.setVgrow(content, Priority.ALWAYS);
        container.getChildren().addAll(content);


        scene.getStylesheets().addAll(Static.CSS_FILES);
        scene.getStylesheets().addAll("style/default/toolbar.css", "style/default/general_style.css");

        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
        stage.setScene(scene);
        stage.show();

        Updater.checkForUpdates(null, false);

        toolBar.updateButtons();
    }

    private void addCopyright(StackPane pane){
        Label label = new Label("© strukteon - strukteon.net");
        label.setTextFill(Color.WHITE);
        label.setOpacity(0.7);
        label.setMouseTransparent(true);
        label.setPadding(new Insets(10, 10, 10, 10));

        StackPane.setAlignment(label, Pos.BOTTOM_LEFT);
        pane.getChildren().add(label);
    }


}
