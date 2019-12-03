package com.kvrmnks.data;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MyDialog {
    public static void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR, content);
        Platform.runLater(alert::showAndWait);
    }

    public static void showInformationAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content);
        Platform.runLater(alert::showAndWait);
        //alert.showAndWait();
    }

    public static String showTextInputDialog(String content) {
        FutureTask<Optional<String>> query = new FutureTask<Optional<String>>(
                new Callable<Optional<String>>() {
                    @Override
                    public Optional<String> call() throws Exception {
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.setContentText(content);

                        return dialog.showAndWait();
                    }
                }
        );
        Platform.runLater(query);
        try {
            return query.get().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean showCheckAlert(String content) {
        FutureTask<Optional<ButtonType>> query = new FutureTask<Optional<ButtonType>>(
                new Callable<Optional<ButtonType>>() {
                    @Override
                    public Optional<ButtonType> call() throws Exception {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText(content);
                        Optional<ButtonType> ret = alert.showAndWait();
                        return ret;
                    }
                }
        );
        Platform.runLater(query);
        try {
            return query.get().get() == ButtonType.OK;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }
}
