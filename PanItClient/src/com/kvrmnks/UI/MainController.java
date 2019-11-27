package com.kvrmnks.UI;

import com.kvrmnks.Main;
import com.kvrmnks.data.MyDialog;
import com.kvrmnks.data.SimpleMyFileProperty;
import com.kvrmnks.net.Client;
import com.kvrmnks.data.MyFile;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public TableView fileTableView;
    public TableColumn fileTypeTableColumn;
    public TableColumn fileNameTableColumn;
    public TableColumn fileSizeTableColumn;
    public TableColumn fileModifyTimeTableColumn;
    public ContextMenu contextMenu;
    public MenuItem openMenuItem;
    public MenuItem downLoadMenuItem;
    public MenuItem renameMenuItem;
    public MenuItem removeMenuItem;
    public MenuItem bindMenuItem;
    public TextField pathTextField;
    public Button backForwardButton;
    private Main application;
    private Client client;
    private ObservableList<SimpleMyFileProperty> data = FXCollections.observableArrayList();
    private SimpleMyFileProperty simpleMyFileProperty;
    //private String currentPath = "";
    private SimpleStringProperty currentPath = new SimpleStringProperty("/");
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        fileNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        fileSizeTableColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        fileModifyTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("modifyTime"));
        fileTableView.setItems(data);

        fileTableView.setRowFactory(new Callback<TableView, TableRow>() {
            @Override
            public TableRow call(TableView param) {
                TableRow<SimpleMyFileProperty> row = new TableRow<>();
                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getClickCount() == 2){
                            TableRow<SimpleMyFileProperty> r = (TableRow<SimpleMyFileProperty>)event.getSource();
                            setSimpleMyFileProperty(r.getItem());
                            try {
                                open(null);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (event.getButton() == MouseButton.SECONDARY) {
                            contextMenu.show(fileTableView, event.getScreenX(), event.getScreenY());
                            TableRow<SimpleMyFileProperty> r = (TableRow<SimpleMyFileProperty>)event.getSource();
                            setSimpleMyFileProperty(r.getItem());
                        }
                    }
                });

                return row;
            }
        });

    }

    void setSimpleMyFileProperty(SimpleMyFileProperty viewMyFile){
        this.simpleMyFileProperty = viewMyFile;
    }

    public void setApp(Main app) {
        application = app;
    }

    public void init() throws IOException {
        pathTextField.textProperty().bindBidirectional(currentPath);
        flush();
    }

    public void setClient(Socket s, DataInputStream dis, DataOutputStream dos) {
        client = new Client(dis, dos, s);
    }

    public void flush() throws IOException {
        data.clear();
        MyFile[] file = client.getStructure(currentPath.getValueSafe());
        for (MyFile mf : file) {
            data.add(new SimpleMyFileProperty(mf));
        }
    }

    public void open(ActionEvent actionEvent) throws IOException {
        if(simpleMyFileProperty.getType().equals("文件夹")){
            currentPath.setValue(currentPath.getValueSafe() + simpleMyFileProperty.getName() + "/");
            flush();
        }
    }

    public void download(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择下载目录");
        File f = directoryChooser.showDialog(application.getStage());
        if(f == null) return;
        client.downLoad(currentPath.getValueSafe()+simpleMyFileProperty.getName()
                ,simpleMyFileProperty.getName()
                ,f,application.getServerIp());
    }

    public void reName(ActionEvent actionEvent) {
        try {
            String newName = MyDialog.showTextInputDialog("输入新的文件名");
            if(newName == null || newName.equals(""))return;
            client.reName(currentPath.getValueSafe()+simpleMyFileProperty.getName()
                    ,currentPath.getValueSafe()+newName);
            flush();
            //simpleMyFileProperty.setName(newName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(ActionEvent actionEvent) {
        try {
            if(MyDialog.showCheckAlert("是否确定删除"))
                client.delete(currentPath.getValueSafe() + simpleMyFileProperty.getName());
            flush();
            //data.remove(simpleMyFileProperty);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void binding(ActionEvent actionEvent) {
    }

    public void backForward(ActionEvent actionEvent) throws IOException {
        currentPath.setValue(MyFile.backFroward(currentPath.getValueSafe()));
        flush();
    }
}
