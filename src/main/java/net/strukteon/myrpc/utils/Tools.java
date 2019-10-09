package net.strukteon.myrpc.utils;

/*
    Created by nils on 18.01.2019 at 17:46.
    Project: My Rich Presence
    
    (c) Nils 2019
    strukteon.net
*/

import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Tools {

    public static Alert generateAlert(Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().addAll(Static.CSS_FILES);
        alert.getDialogPane().getStylesheets().add("style/default/discord_style/dialog.css");
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Tools.class.getResourceAsStream("/icon.png")));
        return alert;
    }

    public static TextInputDialog generateTextInputDialog(){
        TextInputDialog tid = new TextInputDialog();
        tid.setGraphic(null);
        tid.getDialogPane().getStylesheets().addAll(Static.CSS_FILES);
        tid.getDialogPane().getStylesheets().add("style/default/discord_style/dialog.css");
        ((Stage) tid.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Tools.class.getResourceAsStream("/icon.png")));
        return tid;
    }

    public static TextInputDialog generateIntegerInputDialog(){
        TextInputDialog tid = generateTextInputDialog();
        TextField tf = tid.getEditor();
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty())
                tf.setText("0");
            else if (newValue.matches("0\\d+"))
                tf.setText(newValue.substring(1));
            else if (!newValue.matches("\\d+"))
                tf.setText(oldValue);
        });
        return tid;
    }

    public static ChoiceDialog<String> generateChoiceDialog(){
        ChoiceDialog<String> cd = new ChoiceDialog<>();
        cd.setGraphic(null);
        cd.getDialogPane().getStylesheets().addAll(Static.CSS_FILES);
        cd.getDialogPane().getStylesheets().add("style/default/discord_style/dialog.css");
        ((Stage) cd.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Tools.class.getResourceAsStream("/icon.png")));
        return cd;
    }

    /**
     * @return will return > 0 if version1 is higher, < 0 if version2 is higher and 0 if the versions are the same
     */
    public static int compareVersions(String version1, String version2){
        String[] splitV1 = version1.split("[.]");
        String[] splitV2 = version2.split("[.]");
        int len;

        if (splitV1.length > splitV2.length)
            len = splitV1.length;
        else
            len = splitV2.length;

        for (int i = 0; i < len; i++){
            int v1 = i < splitV1.length ? Integer.parseInt(splitV1[i]) : 0;
            int v2 = i < splitV2.length ? Integer.parseInt(splitV2[i]) : 0;
            if (v1 == v2)
                continue;
            return v1 - v2;
        }

        return 0;
    }

}
