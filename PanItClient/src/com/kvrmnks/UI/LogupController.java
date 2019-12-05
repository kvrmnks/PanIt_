package com.kvrmnks.UI;

import com.kvrmnks.Main;
import com.kvrmnks.data.MyDialog;
import com.kvrmnks.net.Client;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LogupController implements Initializable {
    public Button logupButton;
    public Button backButton;
    public TextField userNameTextField;
    public PasswordField passwordTextField;
    public PasswordField passwordCheckTextField;
    public Label passwordCheckLabel;
    private Main application;

    public void setApp(Main app) {
        application = app;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void logup(ActionEvent actionEvent) {
        if (!passwordTextField.getText().equals(passwordCheckTextField.getText()))
            return;
        try {
            Boolean flag = Client.logup(userNameTextField.getText(), passwordTextField.getText());
            if (!flag) {
                MyDialog.showErrorAlert("注册失败");
            } else {
                MyDialog.showInformationAlert("注册成功！");
                application.setLoginForm();
            }
        } catch (IOException e) {
            e.printStackTrace();
            MyDialog.showErrorAlert("注册失败");
        }

    }

    public void back(ActionEvent actionEvent) {
        application.setLoginForm();
    }


    public void passwordCheck2(ActionEvent actionEvent) {

    }

    public void passwordCheck(KeyEvent keyEvent) {
        String password = passwordTextField.getText();
        String password2 = passwordCheckTextField.getText();
        if (password.equals("")) {
            passwordCheckLabel.setText("");
        } else {
            if (password.equals(password2)) {
                passwordCheckLabel.setText("√");
                passwordCheckLabel.setTextFill(Color.GREEN);
            } else {
                passwordCheckLabel.setText("×");
                passwordCheckLabel.setTextFill(Color.RED);
            }
        }
    }
}
