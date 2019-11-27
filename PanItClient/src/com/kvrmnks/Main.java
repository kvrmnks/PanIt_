package com.kvrmnks;

import com.kvrmnks.UI.ConnectController;
import com.kvrmnks.UI.LoginController;
import com.kvrmnks.UI.MainController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class Main extends Application {
    private Stage stage;
    private String serverIp;

    public String getServerIp() {
        return serverIp;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("PanTa");
        setConnectForm();
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        GridPane page;
        try {
            page = (GridPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    private Initializable replaceSceneContentForBorder(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        BorderPane page;
        try {
            page = (BorderPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    public void setConnectForm() {
        try {
            ConnectController connect = (ConnectController) replaceSceneContent("ConnectFXML.fxml");
            connect.setApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLoginForm(Socket s,String serverIp) {
        try {
            this.serverIp = serverIp;
            LoginController login = (LoginController) replaceSceneContent("LoginFXML.fxml");
            login.setApp(this);
            login.setSocket(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainForm(Socket s, DataInputStream in, DataOutputStream out) {
        try {
            MainController mainform = (MainController) replaceSceneContentForBorder("MainFXML.fxml");
            mainform.setApp(this);
            mainform.setClient(s, in, out);
            mainform.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
