package net.strukteon.myrpc.control;

/*
    Created by nils on 13.01.2019 at 19:50.
    Project: My Rich Presence
    
    (c) Nils 2018
    strukteon.net
*/

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.strukteon.myrpc.settings.Application;
import net.strukteon.myrpc.settings.Updater;
import net.strukteon.myrpc.utils.Logger;
import net.strukteon.myrpc.utils.Static;
import net.strukteon.myrpc.utils.Tools;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class ActionHandlers {

    private Interface gui;

    public ActionHandlers(Interface gui) {
        this.gui = gui;
    }

    public void openDonatePage(ActionEvent e){
        MyRichPresence.openWebpage(Static.DONATE_PAGE);
    }

    public void openPreview(ActionEvent e){
        MyRichPresence.openWebpage(String.format("https://discordapp.com/developers/applications/%s/rich-presence/visualizer", gui.myRichPresence.getCurrentProfile().appId));
    }

    public void openTutorial(ActionEvent e){
        MyRichPresence.openWebpage("https://www.youtube.com/watch?v=LcL5GrcNisI");
    }


    public void onRemoveImageClick(ActionEvent e){
        ChoiceDialog<String> cd = Tools.generateChoiceDialog();
        cd.getItems().addAll(gui.myRichPresence.getCurrentProfile().customImages);
        cd.setHeaderText("Select an image key to remove");
        Optional<String> answer = cd.showAndWait();
        if (answer.isPresent()) {
            gui.myRichPresence.getCurrentProfile().customImages.remove(answer.get());
            gui.updateImageKeys();
            Logger.LOG("Removed the image key \"%s\"", answer.get());
        }
    }

    public void onAddImageClick(ActionEvent e){
        TextInputDialog tid = Tools.generateTextInputDialog();
        tid.setHeaderText("Add a new image key");
        Optional<String> answer = tid.showAndWait();
        if (answer.isPresent() && ! gui.myRichPresence.getCurrentProfile().customImages.contains(answer.get())) {
            gui.myRichPresence.getCurrentProfile().customImages.add(answer.get());
            gui.updateImageKeys();
            Logger.LOG("Added the image key \"%s\"", answer.get());
        }
    }

    public void onRemoveProfileClick(ActionEvent e){
        ChoiceDialog<String> cd = Tools.generateChoiceDialog();
        cd.getItems().addAll(gui.myRichPresence.getProfileNames());
        cd.setHeaderText("Select a profile to remove");
        Optional<String> answer = cd.showAndWait();
        if (answer.isPresent()) {
            int removeIndex = gui.myRichPresence.getProfileNames().indexOf(answer.get());
            gui.myRichPresence.removeProfile(removeIndex);
        }
    }

    public void onAddProfileClick(ActionEvent e){
        TextInputDialog tid = Tools.generateTextInputDialog();
        tid.setHeaderText("Create a new profile");
        Optional<String> answer = tid.showAndWait();
        if (answer.isPresent() && ! gui.myRichPresence.getProfiles().stream()
                .map(p -> p.profileName.toLowerCase()).collect(Collectors.toList()).contains(answer.get().toLowerCase())) {
            try {
                gui.myRichPresence.createProfile(answer.get());
                gui.bt_remove_profile.setDisable(false);
            } catch (IOException ex) {
                Logger.LOG("An error ocurred while trying to create a profile");
            }
        }
    }


    public void onCurrentPlayersChange(ObservableValue<? extends String> observable, String oldValue, String newValue){
        if (newValue.matches("0[0-9]"))
            gui.tf_group_cur.setText(newValue.substring(1));
        else if (newValue.isEmpty())
            gui.tf_group_cur.setText("0");
        else if (!newValue.matches("[0-9]|[1-9][0-9]{1,3}"))
            gui.tf_group_cur.setText(oldValue);
    }

    public void onMaximumPlayersChange(ObservableValue<? extends String> observable, String oldValue, String newValue){
        if (newValue.matches("0[0-9]"))
            gui.tf_group_max.setText(newValue.substring(1));
        else if (newValue.isEmpty())
            gui.tf_group_max.setText("0");
        else if (!newValue.matches("[0-9]|[1-9][0-9]{1,3}"))
            gui.tf_group_max.setText(oldValue);
    }


    public void onStopPresenceClick(ActionEvent e){
        gui.myRichPresence.stopPresence();
    }

    public void onUpdatePresenceClick(ActionEvent e){
        if (!gui.myRichPresence.isRunning())
            gui.bt_update_presence.setText("Update Presence");
        try {
            gui.myRichPresence.storeProfile();
            gui.myRichPresence.getCurrentProfile().saveProfile();
            gui.myRichPresence.updatePresence();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    public void onCheckUpdatesClick(ActionEvent e){
        try {
            Updater.checkForUpdates(gui.myRichPresence.stage, true);
        } catch (IOException e1) {
            Logger.LOG("An error occurred while trying to update: %s", e1.getMessage());
        }
    }


    public void onTimerSettingsClick(ActionEvent e){
        ChoiceDialog<String> cd = Tools.generateChoiceDialog();
        cd.getItems().addAll("Time elapsed", "Time remaining");
        cd.setHeaderText("Select a timer mode");
        cd.setSelectedItem(cd.getItems().get(0));

        Optional<String> mode = cd.showAndWait();

        if (mode.isPresent() && mode.get().equals(cd.getItems().get(0))){
            gui.myRichPresence.getCurrentProfile().isCountingUp = true;
        } else if (mode.isPresent() && mode.get().equals(cd.getItems().get(1))){
            TextInputDialog tid = Tools.generateIntegerInputDialog();
            tid.setHeaderText("Amount of seconds remaining");
            Optional<String> seconds = tid.showAndWait();
            if (seconds.isPresent()) {
                gui.myRichPresence.getCurrentProfile().isCountingUp = false;
                gui.myRichPresence.getCurrentProfile().countdownSeconds = Integer.parseInt(seconds.get());
            }
        }
    }

    public void onSettingsClick(ActionEvent e) {
        gui.myRichPresence.storeSettings();
    }

    public void onDownloadExtensionClick(ActionEvent e){
        ChoiceDialog<String> cd = Tools.generateChoiceDialog();
        cd.getItems().addAll("Firefox", "Chrome");
        cd.setHeaderText("Select a browser");

        cd.setSelectedItem(cd.getItems().get(0));

        Optional<String> opt = cd.showAndWait();
        if (opt.isPresent()){
            if (cd.getItems().get(0).equals(opt.get()))
                MyRichPresence.openWebpage(Static.EXTENSION_FIREFOX);
            else
                MyRichPresence.openWebpage(Static.EXTENSION_CHROME);
        }
    }



    /* public void onRemoveApplicationClick(ActionEvent e){ // TODO
        ChoiceDialog<String> cd = Tools.generateChoiceDialog();
        cd.getItems().addAll(gui.myRichPresence.settings.applications.stream().map(a -> a.name).collect(Collectors.toList()));
        cd.setHeaderText("Select an application to remove");
        Optional<String> answer = cd.showAndWait();
        if (answer.isPresent()) {
            int removeIndex = gui.myRichPresence.settings.applications.stream().map(a -> a.name).collect(Collectors.toList()).indexOf(answer.get());
            gui.myRichPresence.removeApplication(removeIndex);
        }
    } */

    /*public void onAddApplicationClick(ActionEvent e){ // TODO

        Dialog<Application> dialog = new Dialog<>();
        dialog.setGraphic(null);
        dialog.getDialogPane().getStylesheets().addAll(Static.CSS_FILES);
        dialog.getDialogPane().getStylesheets().add("style/default/discord_style/dialog.css");
        ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Tools.class.getResourceAsStream("/icon.png")));

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Label lb_name = new Label("Name (optional)");
        Label lb_id = new Label("Application ID (required)");
        TextField appName = new TextField();
        TextField appID = new TextField();
        dialogPane.setContent(new VBox(8, lb_name, appName, lb_id, appID));

        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                Application application = new Application();
                if (!appName.getText().isEmpty())
                    application.name = appName.getText();
                else
                    application.name = appID.getText();
                application.id = appID.getText();
                application.images = new ArrayList<>();
                return application;
            }
            return null;
        });

        appID.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*"))
                    appID.setText(oldValue);
                else dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(newValue.isEmpty());
            }
        });

        dialog.setHeaderText("Add an application");
        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        Optional<Application> answer = dialog.showAndWait();
        if (answer.isPresent() && ! gui.myRichPresence.settings.applications.stream()
                .map(a -> a.id).collect(Collectors.toList()).contains(answer.get().id)) {
            gui.myRichPresence.createApplication(answer.get());
            gui.bt_remove_application.setDisable(false);
        }
    } */

}
