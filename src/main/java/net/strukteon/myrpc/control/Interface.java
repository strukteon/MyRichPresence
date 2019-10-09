package net.strukteon.myrpc.control;

/*
    Created by nils on 02.01.2019 at 20:27.
    Project: My Rich Presence

    (c) Nils 2019
    strukteon.net
*/


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import net.strukteon.myrpc.utils.Logger;
import net.strukteon.myrpc.utils.Static;
import net.strukteon.myrpc.settings.Profile;
import net.strukteon.myrpc.utils.TempInterfaceState;

import java.util.stream.Collectors;

public class Interface {

    private ActionHandlers actionHandlers;
    private Node content;
    public MyRichPresence myRichPresence;
    public int mode = 0;

    public Button bt_download_ext;
    public Button bt_add_profile;
    public Button bt_remove_profile;
    public Button bt_timer_settings;
    public Button bt_stop_presence;
    public Button bt_update_presence;
    public Button bt_view_preview_pg1;
    public Button bt_add_image;
    public Button bt_remove_image;
    public Button bt_remove_all_images;
    public Button bt_view_preview_pg2;
    public Button bt_donate;
    public Button bt_check_updates;
    public Button bt_tutorial;
    // public Button bt_add_application; // TODO
    // public Button bt_remove_application; // TODO
    public Button bt_export_to_jar;

    public ChoiceBox<String> cb_profiles;
    public ChoiceBox<String> cb_mode;
    // public ChoiceBox<String> cb_application; // TODO
    public ChoiceBox<String> cb_large_image;
    public ChoiceBox<String> cb_small_image;

    public CheckBox ch_show_timer;
    public CheckBox ch_large_img_enabled;
    public CheckBox ch_small_img_enabled;
    public CheckBox ch_minimize_to_taskbar;
    public CheckBox ch_resizable;

    public TextArea ta_debug_log;

    public TextField tf_state;
    public TextField tf_details;
    public TextField tf_group_cur;
    public TextField tf_group_max;
    public TextField tf_app_id;
    public TextField tf_large_image;
    public TextField tf_small_image;

    public Label lb_version;
    public Label lb_profile;
    public Label lb_details;
    public Label lb_state;
    public Label lb_group;
    public Label lb_group_cur;
    public Label lb_group_max;
    public Label lb_app_id;
    public Label lb_large_img;
    public Label lb_small_img;
    public Label lb_view_preview_pg1;
    public Label lb_view_preview_pg2;

    public WebView wv_update_log;

    public int presenceMode = 0;
    

    public Interface(Node content, MyRichPresence myRichPresence){
        this.content = content;
        this.myRichPresence = myRichPresence;
        actionHandlers = new ActionHandlers(this);

        initVariables();
        setup();
    }
    
    void initVariables(){
        bt_download_ext         = (Button) content.lookup("#bt_download_ext");
        bt_add_profile          = (Button) content.lookup("#bt_add_profile");
        bt_remove_profile       = (Button) content.lookup("#bt_remove_profile");
        bt_timer_settings       = (Button) content.lookup("#bt_timer_settings");
        bt_stop_presence        = (Button) content.lookup("#bt_stop_presence");
        bt_update_presence      = (Button) content.lookup("#bt_update_presence");
        bt_view_preview_pg1     = (Button) content.lookup("#bt_view_preview_pg1");
        bt_add_image            = (Button) content.lookup("#bt_add_image");
        bt_remove_image         = (Button) content.lookup("#bt_remove_image");
        bt_remove_all_images    = (Button) content.lookup("#bt_remove_all_images");
        bt_view_preview_pg2     = (Button) content.lookup("#bt_view_preview_pg2");
        bt_donate               = (Button) content.lookup("#bt_donate");
        bt_check_updates        = (Button) content.lookup("#bt_check_updates");
        bt_tutorial             = (Button) content.lookup("#bt_tutorial");
        // bt_add_application      = (Button) content.lookup("#bt_add_application");
        // bt_remove_application   = (Button) content.lookup("#bt_remove_application");
        bt_export_to_jar        = (Button) content.lookup("#bt_export_to_jar");

        cb_profiles             = (ChoiceBox<String>) content.lookup("#cb_profiles");
        cb_mode                 = (ChoiceBox<String>) content.lookup("#cb_mode");
        // cb_application          = (ChoiceBox<String>) content.lookup("#cb_application");
        cb_large_image          = (ChoiceBox<String>) content.lookup("#cb_large_image");
        cb_small_image          = (ChoiceBox<String>) content.lookup("#cb_small_image");

        ch_show_timer           = (CheckBox) content.lookup("#ch_show_timer");
        ch_large_img_enabled    = (CheckBox) content.lookup("#ch_large_img_enabled");
        ch_small_img_enabled    = (CheckBox) content.lookup("#ch_small_img_enabled");
        ch_minimize_to_taskbar  = (CheckBox) content.lookup("#ch_minimize_to_taskbar");
        ch_resizable            = (CheckBox) content.lookup("#ch_resizable");

        ta_debug_log            = (TextArea) content.lookup("#ta_debug_log");

        tf_state                = (TextField) content.lookup("#tf_state");
        tf_details              = (TextField) content.lookup("#tf_details");
        tf_group_cur            = (TextField) content.lookup("#tf_group_cur");
        tf_group_max            = (TextField) content.lookup("#tf_group_max");
        tf_app_id               = (TextField) content.lookup("#tf_app_id");
        tf_large_image          = (TextField) content.lookup("#tf_large_image");
        tf_small_image          = (TextField) content.lookup("#tf_small_image");

        lb_version              = (Label) content.lookup("#lb_version");
        lb_profile              = (Label) content.lookup("#lb_profile");
        lb_details              = (Label) content.lookup("#lb_details");
        lb_state                = (Label) content.lookup("#lb_state");
        lb_group                = (Label) content.lookup("#lb_group");
        lb_group_cur            = (Label) content.lookup("#lb_group_cur");
        lb_group_max            = (Label) content.lookup("#lb_group_max");
        lb_app_id               = (Label) content.lookup("#lb_app_id");
        lb_large_img            = (Label) content.lookup("#lb_large_img");
        lb_small_img            = (Label) content.lookup("#lb_small_img");
        lb_view_preview_pg1     = (Label) content.lookup("#lb_view_preview_pg1");
        lb_view_preview_pg2     = (Label) content.lookup("#lb_view_preview_pg2");

        wv_update_log           = (WebView) content.lookup("#wv_update_log");

        Logger.initializeTextArea(ta_debug_log);
        Logger.LOG("Successfully initialized all variables");
    }

    void setup(){

        bt_download_ext.setDisable(true);
        bt_download_ext.setOnAction(actionHandlers::onDownloadExtensionClick);

        cb_mode.getItems().addAll(Static.PRESENCE_MODES);
        cb_mode.getSelectionModel().select(mode);
        cb_mode.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                switchMode(newValue.intValue());
            }
        });

        // cb_application.getItems().addAll(myRichPresence.settings.applications.stream().map(a -> a.name != null ? a.name : a.id).collect(Collectors.toList())); // TODO
        // cb_application.getSelectionModel().selectFirst(); // TODO

        // bt_remove_application.setDisable(myRichPresence.settings.applications.size() <= 1); // TODO

        // bt_add_application.setOnAction(actionHandlers::onAddApplicationClick); // TODO
        // bt_remove_application.setOnAction(actionHandlers::onRemoveApplicationClick); // TODO

        lb_version.setText(String.format("Version %s", Static.VERSION));
        bt_donate.setOnAction(actionHandlers::openDonatePage);
        bt_tutorial.setOnAction(actionHandlers::openTutorial);

        bt_view_preview_pg1 .setOnAction(actionHandlers::openPreview);
        bt_view_preview_pg2 .setOnAction(actionHandlers::openPreview);

        ch_large_img_enabled.setOnAction(e -> {
            cb_large_image.setDisable(! ch_large_img_enabled.isSelected());
            tf_large_image.setDisable(! ch_large_img_enabled.isSelected());
        });

        ch_small_img_enabled.setOnAction(e -> {
            cb_small_image.setDisable(! ch_small_img_enabled.isSelected());
            tf_small_image.setDisable(! ch_small_img_enabled.isSelected());
        });

        bt_add_image.setOnAction(actionHandlers::onAddImageClick);
        bt_remove_image.setOnAction(actionHandlers::onRemoveImageClick);
        bt_remove_all_images.setOnAction(e -> {
            myRichPresence.getCurrentProfile().customImages.clear();
            updateImageKeys();
        });

        bt_add_profile.setOnAction(actionHandlers::onAddProfileClick);

        bt_remove_profile.setOnAction(actionHandlers::onRemoveProfileClick);

        ch_show_timer.setOnAction(e -> bt_timer_settings.setDisable(! ch_show_timer.isSelected()));
        bt_timer_settings.setOnAction(actionHandlers::onTimerSettingsClick);


        tf_app_id.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf_app_id.setText(oldValue);
                return;
            }
            bt_add_image.setDisable(newValue.equals(Static.DEFAULT_APP_ID));
            bt_remove_image.setDisable(newValue.equals(Static.DEFAULT_APP_ID));
            bt_remove_all_images.setDisable(newValue.equals(Static.DEFAULT_APP_ID));
        });

        cb_profiles.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if ((int) oldValue > -1)
                myRichPresence.storeProfile();
            myRichPresence.stopPresence();
            if ((int) newValue > -1) {
                myRichPresence.currentProfile = (int) newValue;
                loadProfile(myRichPresence.getCurrentProfile());
            }
        });

        bt_update_presence.setOnAction(actionHandlers::onUpdatePresenceClick);
        bt_stop_presence.setOnAction(actionHandlers::onStopPresenceClick);

        tf_group_cur.textProperty().addListener(actionHandlers::onCurrentPlayersChange);
        tf_group_max.textProperty().addListener(actionHandlers::onMaximumPlayersChange);

        bt_check_updates.setOnAction(actionHandlers::onCheckUpdatesClick);

        ch_minimize_to_taskbar.setOnAction(actionHandlers::onSettingsClick);
        ch_resizable.setOnAction(actionHandlers::onSettingsClick);

        Logger.LOG("Successfully set all listeners");

        System.out.println(wv_update_log);
        System.out.println(wv_update_log.getEngine());
        wv_update_log.getEngine().load(Static.UPDATE_LOG);
    }

    public void updateImageKeys(){
        cb_large_image.getItems().setAll(myRichPresence.getCurrentProfile().customImages);
        cb_small_image.getItems().setAll(myRichPresence.getCurrentProfile().customImages);
        bt_remove_image.setDisable(myRichPresence.getCurrentProfile().customImages.size() == 0);
        bt_remove_all_images.setDisable(myRichPresence.getCurrentProfile().customImages.size() == 0);
    }

    public void loadProfile(Profile profile){
        tf_state            .setText(profile.state);
        tf_details          .setText(profile.details);
        ch_show_timer       .setSelected(profile.showTimer);
        bt_timer_settings   .setDisable(!profile.showTimer);
        tf_group_cur        .setText("" + profile.curPlayers);
        tf_group_max        .setText("" + profile.maxPlayers);

        tf_app_id           .setText(profile.appId);

        ch_large_img_enabled.setSelected(profile.largeImage.enabled);
        cb_large_image      .getItems().clear();
        cb_large_image      .getItems().addAll(profile.customImages);
        cb_large_image      .getSelectionModel().select(profile.largeImage.imageKey);
        tf_large_image      .setText(profile.largeImage.imageText);

        cb_large_image      .setDisable(! profile.largeImage.enabled);
        tf_large_image      .setDisable(! profile.largeImage.enabled);

        ch_small_img_enabled.setSelected(profile.smallImage.enabled);
        cb_small_image      .getItems().clear();
        cb_small_image      .getItems().addAll(profile.customImages);
        cb_small_image      .getSelectionModel().select(profile.smallImage.imageKey);
        tf_small_image      .setText(profile.smallImage.imageText);

        cb_small_image      .setDisable(! profile.smallImage.enabled);
        tf_small_image      .setDisable(! profile.smallImage.enabled);


        bt_add_image        .setDisable(profile.appId.equals(Static.DEFAULT_APP_ID));
        bt_remove_image     .setDisable(profile.customImages.size() == 0 || profile.appId.equals(Static.DEFAULT_APP_ID));
        bt_remove_all_images.setDisable(profile.customImages.size() == 0 || profile.appId.equals(Static.DEFAULT_APP_ID));

        Logger.LOG("Loaded the profile \"%s\"", profile.profileName);
    }

    public void switchMode(int newMode){
        myRichPresence.stopPresence();
        presenceMode = newMode;
        bt_download_ext.setDisable(!bt_download_ext.isDisabled());
        if (newMode == 1){
            TempInterfaceState.store(this);
            TempInterfaceState.disableAll(this);
        } else if (newMode == 0) {
            TempInterfaceState.restore(this);
        }
    }
    
}
