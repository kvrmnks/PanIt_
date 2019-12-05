package com.kvrmnks;

import com.kvrmnks.UI.BuilderController;
import com.kvrmnks.UI.MainController;
import com.kvrmnks.net.Server;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.InputStream;
import java.net.ServerSocket;

public class Main extends Application {
    private Stage stage;
    private Scene curScene;

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
        curScene = new Scene(page);
        stage.setScene(curScene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    private Initializable replaceSceneContentForTab(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        TabPane page;
        try {
            page = (TabPane) loader.load(in);
        } finally {
            in.close();
        }
        curScene = new Scene(page);
        stage.setScene(curScene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("PanTa");
        setBuilderForm();
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    void setBuilderForm() {
        try {
            BuilderController bc = (BuilderController) replaceSceneContent("BuilderFXML.fxml");
            bc.setApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainForm(ServerSocket ss) {
        try {
            MainController mc = (MainController) replaceSceneContentForTab("MainFXML.fxml");
            mc.setApp(this);
            Server s = new Server(ss, mc);
            Thread t = new Thread(s);
            t.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
