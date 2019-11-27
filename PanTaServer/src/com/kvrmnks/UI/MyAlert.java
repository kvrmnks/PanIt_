package com.kvrmnks.UI;

import javafx.scene.control.Alert;

public class MyAlert {
    public static void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR, content);
        alert.showAndWait();
    }

    public static void showInformationAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content);
        alert.showAndWait();
    }
}
