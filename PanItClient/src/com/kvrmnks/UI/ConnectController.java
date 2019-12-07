package com.kvrmnks.UI;

import com.kvrmnks.Main;
import com.kvrmnks.data.MyDialog;
import com.kvrmnks.exception.Log;
import com.kvrmnks.net.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class ConnectController implements Initializable {
    public Button connectButton, closeButton;
    public TextField ipTextField, portTextField;
    private Main application;

    public void setApp(Main app) {
        application = app;
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void connect(ActionEvent actionEvent) {

        try {
            String ip = ipTextField.getText();
            int port = Integer.parseInt(portTextField.getText());
            Socket socket = new Socket(ip, port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            Client.setClient(in, out, socket);
            Client.setServerIp(ip);
            Log.log("链接成功 ip:" + ip + " port:" + port);
            application.setLoginForm();
        } catch (NumberFormatException e) {
            MyDialog.showErrorAlert("端口格式有误");
        } catch (UnknownHostException e) {
            MyDialog.showInformationAlert("找不到服务器");
        } catch (IOException e) {
            e.printStackTrace();
            MyDialog.showErrorAlert("无法连接");
        }

    }

    public void close(ActionEvent actionEvent) {
        Platform.exit();
    }
}

