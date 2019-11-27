package com.kvrmnks.UI;

import com.kvrmnks.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class BuilderController implements Initializable {
    Main application;
    @FXML
    public Button closeButton, buildButton;
    @FXML
    public TextField portTextField;
    @FXML
    public PasswordField passwordTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setApp(Main app) {
        application = app;
    }

    @FXML
    public void close(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    public void build(ActionEvent actionEvent) {
        try {
            ServerSocket ss = new ServerSocket(Integer.parseInt(portTextField.getText()));
            application.setMainForm(ss);
        } catch (NumberFormatException e) {
            MyAlert.showErrorAlert("端口格式有误");
        } catch (IOException e) {
            MyAlert.showInformationAlert("无法建立服务器");
        }
    }
}
