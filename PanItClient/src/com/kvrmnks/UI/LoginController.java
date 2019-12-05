package com.kvrmnks.UI;

import com.kvrmnks.Main;
import com.kvrmnks.data.MD5;
import com.kvrmnks.data.MyDialog;
import com.kvrmnks.net.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Main application;
    @FXML
    public Button closeButton, loginButton;
    public TextField userNameTextField;
    public PasswordField passwordTextField;
    private Socket socket;
    private String serverIp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void close(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    public void login(ActionEvent actionEvent) {
        try {
            DataInputStream in = Client.getSocketIn();
            DataOutputStream out = Client.getSocketOut();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(userNameTextField.getText());
            stringBuilder.append("$");
            stringBuilder.append(MD5.getMD5(passwordTextField.getText()));
            out.writeUTF(stringBuilder.toString());
            boolean flag = false;
            flag = in.readBoolean();
            if (flag) {
                application.setMainForm();
            } else {
                MyDialog.showInformationAlert("用户名或密码错误");
            }
        } catch (IOException e) {
            MyDialog.showErrorAlert("连接失败");
            e.printStackTrace();
        }
    }

    public void setSocket(Socket s) {
        socket = s;
    }

    public void setApp(Main app) {
        application = app;
    }

    public void logup(ActionEvent actionEvent) {
        application.setLogupForm();
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
