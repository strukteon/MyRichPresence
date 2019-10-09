package net.strukteon.myrpc.utils;

/*
    Created by nils on 14.03.2019 at 20:51.
    Project: My Rich Presence

    (c) Nils 2019
    strukteon.net
*/

import net.strukteon.myrpc.control.Interface;

public class TempInterfaceState {

    private static boolean bt_add_profile;
    private static boolean bt_remove_profile;
    private static boolean bt_timer_settings;
    private static boolean bt_stop_presence;
    private static boolean bt_update_presence;
    private static boolean bt_view_preview_pg1;
    private static boolean bt_add_image;
    private static boolean bt_remove_image;
    private static boolean bt_remove_all_images;
    private static boolean bt_view_preview_pg2;
    private static boolean bt_add_application; // TODO
    private static boolean bt_remove_application; // TODO
    private static boolean bt_export_to_jar;

    private static boolean cb_profiles;
    private static boolean cb_large_image;
    private static boolean cb_small_image;

    private static boolean ch_show_timer;
    private static boolean ch_large_img_enabled;
    private static boolean ch_small_img_enabled;

    private static boolean lb_profile;
    private static boolean lb_details;
    private static boolean lb_state;
    private static boolean lb_group;
    private static boolean lb_group_cur;
    private static boolean lb_group_max;
    private static boolean lb_app_id;
    private static boolean lb_large_img;
    private static boolean lb_small_img;
    private static boolean lb_view_preview_pg1;
    private static boolean lb_view_preview_pg2;

    private static boolean tf_state;
    private static boolean tf_details;
    private static boolean tf_group_cur;
    private static boolean tf_group_max;
    private static boolean cb_application; // TODO
    private static boolean tf_app_id; // TODO
    private static boolean tf_large_image;
    private static boolean tf_small_image;

    public static void store(Interface interf) {

        bt_add_profile = interf.bt_add_profile.isDisabled();
        bt_remove_profile = interf.bt_remove_profile.isDisabled();
        bt_timer_settings = interf.bt_timer_settings.isDisabled();
        bt_stop_presence = interf.bt_stop_presence.isDisabled();
        bt_update_presence = interf.bt_update_presence.isDisabled();
        bt_view_preview_pg1 = interf.bt_view_preview_pg1.isDisabled();
        bt_add_image = interf.bt_add_image.isDisabled();
        bt_remove_image = interf.bt_remove_image.isDisabled();
        bt_remove_all_images = interf.bt_remove_all_images.isDisabled();
        bt_view_preview_pg2 = interf.bt_view_preview_pg2.isDisabled();
        cb_profiles = interf.cb_profiles.isDisabled();
        cb_large_image = interf.cb_large_image.isDisabled();
        cb_small_image = interf.cb_small_image.isDisabled();
        ch_show_timer = interf.ch_show_timer.isDisabled();
        ch_large_img_enabled = interf.ch_large_img_enabled.isDisabled();
        ch_small_img_enabled = interf.ch_small_img_enabled.isDisabled();
        lb_profile = interf.lb_profile.isDisabled();
        lb_details = interf.lb_details.isDisabled();
        lb_state = interf.lb_state.isDisabled();
        lb_group = interf.lb_group.isDisabled();
        lb_group_cur = interf.lb_group_cur.isDisabled();
        lb_group_max = interf.lb_group_max.isDisabled();
        lb_app_id = interf.lb_app_id.isDisabled();
        lb_large_img = interf.lb_large_img.isDisabled();
        lb_small_img = interf.lb_small_img.isDisabled();
        lb_view_preview_pg1 = interf.lb_view_preview_pg1.isDisabled();
        lb_view_preview_pg2 = interf.lb_view_preview_pg2.isDisabled();
        tf_state = interf.tf_state.isDisabled();
        tf_details = interf.tf_details.isDisabled();
        tf_group_cur = interf.tf_group_cur.isDisabled();
        tf_group_max = interf.tf_group_max.isDisabled();
        // cb_application = interf.cb_application.isDisabled(); // TODO
        tf_large_image = interf.tf_large_image.isDisabled();
        tf_small_image = interf.tf_small_image.isDisabled();
        // bt_add_application = interf.bt_add_application.isDisabled(); // TODO
        // bt_remove_application = interf.bt_remove_application.isDisabled(); // TODO
        tf_app_id = interf.tf_app_id.isDisabled(); // TODO
        bt_export_to_jar = interf.bt_export_to_jar.isDisabled();

    }

    public static void restore(Interface interf) {
        interf.bt_add_profile.setDisable(bt_add_profile);
        interf.bt_remove_profile.setDisable(bt_remove_profile);
        interf.bt_timer_settings.setDisable(bt_timer_settings);
        interf.bt_stop_presence.setDisable(bt_stop_presence);
        interf.bt_update_presence.setDisable(bt_update_presence);
        interf.bt_view_preview_pg1.setDisable(bt_view_preview_pg1);
        interf.bt_add_image.setDisable(bt_add_image);
        interf.bt_remove_image.setDisable(bt_remove_image);
        interf.bt_remove_all_images.setDisable(bt_remove_all_images);
        interf.bt_view_preview_pg2.setDisable(bt_view_preview_pg2);
        interf.cb_profiles.setDisable(cb_profiles);
        interf.cb_large_image.setDisable(cb_large_image);
        interf.cb_small_image.setDisable(cb_small_image);
        interf.ch_show_timer.setDisable(ch_show_timer);
        interf.ch_large_img_enabled.setDisable(ch_large_img_enabled);
        interf.ch_small_img_enabled.setDisable(ch_small_img_enabled);
        interf.lb_profile.setDisable(lb_profile);
        interf.lb_details.setDisable(lb_details);
        interf.lb_state.setDisable(lb_state);
        interf.lb_group.setDisable(lb_group);
        interf.lb_group_cur.setDisable(lb_group_cur);
        interf.lb_group_max.setDisable(lb_group_max);
        interf.lb_app_id.setDisable(lb_app_id);
        interf.lb_large_img.setDisable(lb_large_img);
        interf.lb_small_img.setDisable(lb_small_img);
        interf.lb_view_preview_pg1.setDisable(lb_view_preview_pg1);
        interf.lb_view_preview_pg2.setDisable(lb_view_preview_pg2);
        interf.tf_state.setDisable(tf_state);
        interf.tf_details.setDisable(tf_details);
        interf.tf_group_cur.setDisable(tf_group_cur);
        interf.tf_group_max.setDisable(tf_group_max);
        // interf.cb_application.setDisable(cb_application); // TODO
        interf.tf_large_image.setDisable(tf_large_image);
        interf.tf_small_image.setDisable(tf_small_image);
        // interf.bt_add_application.setDisable(bt_add_application); // TODO
        // interf.bt_remove_application.setDisable(bt_remove_application); // TODO
        interf.tf_app_id.setDisable(tf_app_id);
        interf.bt_export_to_jar.setDisable(bt_export_to_jar);
    }

    public static void disableAll(Interface interf) {
        interf.bt_add_profile.setDisable(true);
        interf.bt_remove_profile.setDisable(true);
        interf.bt_timer_settings.setDisable(true);
        interf.bt_stop_presence.setDisable(true);
        interf.bt_update_presence.setDisable(true);
        interf.bt_view_preview_pg1.setDisable(true);
        interf.bt_add_image.setDisable(true);
        interf.bt_remove_image.setDisable(true);
        interf.bt_remove_all_images.setDisable(true);
        interf.bt_view_preview_pg2.setDisable(true);
        interf.cb_profiles.setDisable(true);
        interf.cb_large_image.setDisable(true);
        interf.cb_small_image.setDisable(true);
        interf.ch_show_timer.setDisable(true);
        interf.ch_large_img_enabled.setDisable(true);
        interf.ch_small_img_enabled.setDisable(true);
        interf.lb_profile.setDisable(true);
        interf.lb_details.setDisable(true);
        interf.lb_state.setDisable(true);
        interf.lb_group.setDisable(true);
        interf.lb_group_cur.setDisable(true);
        interf.lb_group_max.setDisable(true);
        interf.lb_app_id.setDisable(true);
        interf.lb_large_img.setDisable(true);
        interf.lb_small_img.setDisable(true);
        interf.lb_view_preview_pg1.setDisable(true);
        interf.lb_view_preview_pg2.setDisable(true);
        interf.tf_state.setDisable(true);
        interf.tf_details.setDisable(true);
        interf.tf_group_cur.setDisable(true);
        interf.tf_group_max.setDisable(true);
        // interf.cb_application.setDisable(true); // TODO
        interf.tf_large_image.setDisable(true);
        interf.tf_small_image.setDisable(true);
        // interf.bt_add_application.setDisable(true); // TODO
        // interf.bt_remove_application.setDisable(true); // TODO
        interf.tf_app_id.setDisable(true);
        interf.bt_export_to_jar.setDisable(true);
    }

}
