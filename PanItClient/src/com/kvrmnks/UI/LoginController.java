package com.kvrmnks.UI;

import com.kvrmnks.Main;
import com.kvrmnks.data.MyDialog;
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
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(userNameTextField.getText());
            stringBuilder.append("$");
            stringBuilder.append(passwordTextField.getText());
            out.writeUTF(stringBuilder.toString());
            boolean flag = false;
            flag = in.readBoolean();
            if (flag) {
                application.setMainForm(socket, in, out);
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
}
