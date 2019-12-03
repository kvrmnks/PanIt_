package com.kvrmnks.UI;

import com.kvrmnks.Main;
import com.kvrmnks.data.MyDialog;
import com.kvrmnks.data.SimpleLogListProperty;
import com.kvrmnks.data.SimpleMyFileProperty;
import com.kvrmnks.net.Client;
import com.kvrmnks.data.MyFile;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public TableView<SimpleMyFileProperty> fileTableView;
    public TableColumn<SimpleMyFileProperty, String> fileTypeTableColumn;
    public TableColumn<SimpleMyFileProperty, String> fileNameTableColumn;
    public TableColumn<SimpleMyFileProperty, Long> fileSizeTableColumn;
    public TableColumn<SimpleMyFileProperty, String> fileModifyTimeTableColumn;
    public ContextMenu contextMenu;
    public MenuItem openMenuItem;
    public MenuItem downLoadMenuItem;
    public MenuItem renameMenuItem;
    public MenuItem removeMenuItem;
    public MenuItem bindMenuItem;
    public TextField pathTextField;
    public Button backForwardButton;
    public Button uploadButton;
    public Button flushButton;
    public MenuItem newFileDirectoryMenuList;
    public Button fileSearchButton;
    public TextField fileSearchTextField;
    public TableColumn<SimpleMyFileProperty, String> searchFileNameTableColumn;
    public TableColumn<SimpleMyFileProperty, String> searchFileLocationTableColumn;
    public TableColumn<SimpleMyFileProperty, Long> searchFileSizeTableColumn;
    public TableColumn<SimpleMyFileProperty, String> searchFileModifyTimeTableColumn;
    public TableColumn<SimpleLogListProperty, String> modifyTypeTableColumn;
    public TableColumn<SimpleLogListProperty, String> modifyFileNameTableColumn;
    public TableColumn<SimpleLogListProperty, Long> modifyFileSizeTableColumn;
    public TableColumn<SimpleLogListProperty, ProgressIndicator> modifyProcessTableColumn;
    public TableColumn<SimpleLogListProperty, String> modifyTimeTableColumn;
    public TableView<SimpleMyFileProperty> searchTableView;
    public TableView<SimpleLogListProperty> logTableView;
    private Main application;
    private Client client;
    private ObservableList<SimpleMyFileProperty> data = FXCollections.observableArrayList();
    private ObservableList<SimpleMyFileProperty> searchResult = FXCollections.observableArrayList();
    private ObservableList<SimpleLogListProperty> logdata = FXCollections.observableArrayList();
    private SimpleMyFileProperty simpleMyFileProperty;
    //private String currentPath = "";
    private SimpleStringProperty currentPath = new SimpleStringProperty("/");

    private void initFileTab() {
        fileTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        fileNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        fileSizeTableColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        fileModifyTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("modifyTime"));
        fileTableView.setItems(data);
        fileTableView.setRowFactory(new Callback<TableView<SimpleMyFileProperty>, TableRow<SimpleMyFileProperty>>() {
            @Override
            public TableRow<SimpleMyFileProperty> call(TableView<SimpleMyFileProperty> param) {
                TableRow<SimpleMyFileProperty> row = new TableRow<>();
                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() == 2) {
                            TableRow<SimpleMyFileProperty> r = (TableRow<SimpleMyFileProperty>) event.getSource();
                            setSimpleMyFileProperty(r.getItem());
                            try {
                                open(null);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (event.getButton() == MouseButton.SECONDARY) {
                            contextMenu.show(fileTableView, event.getScreenX(), event.getScreenY());
                            TableRow<SimpleMyFileProperty> r = (TableRow<SimpleMyFileProperty>) event.getSource();
                            setSimpleMyFileProperty(r.getItem());
                        }
                    }
                });

                return row;
            }
        });
    }

    private void initSearchTab() {
        searchFileLocationTableColumn.setCellValueFactory(new PropertyValueFactory<>("path"));
        searchFileModifyTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("modifyTime"));
        searchFileNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        searchFileSizeTableColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        searchTableView.setItems(searchResult);
    }

    private void initLogTab() {
        modifyFileNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        modifyFileSizeTableColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        modifyProcessTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SimpleLogListProperty, ProgressIndicator>, ObservableValue<ProgressIndicator>>() {
            @Override
            public ObservableValue<ProgressIndicator> call(TableColumn.CellDataFeatures<SimpleLogListProperty, ProgressIndicator> param) {
                return new SimpleObjectProperty<ProgressIndicator>(param.getValue().getProgressBar());
            }
        });
        modifyTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        logTableView.setItems(logdata);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFileTab();
        initSearchTab();
        initLogTab();
    }

    void setSimpleMyFileProperty(SimpleMyFileProperty viewMyFile) {
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
        if (simpleMyFileProperty.getType().equals("文件夹")) {
            currentPath.setValue(currentPath.getValueSafe() + simpleMyFileProperty.getName() + "/");
            flush();
        }
    }

    public void download(ActionEvent actionEvent) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择下载目录");
        File f = directoryChooser.showDialog(application.getStage());
        if (f == null) return;
        System.out.println(simpleMyFileProperty.getSize());
        SimpleLogListProperty logListProperty = new SimpleLogListProperty(
                SimpleLogListProperty.TYPE_DOWNLOAD
                , simpleMyFileProperty.getName()
                , ""
                , simpleMyFileProperty.getSize()
                , 0
        );
        logdata.add(logListProperty);
        client.downLoad(
                currentPath.getValueSafe() + simpleMyFileProperty.getName()
                , simpleMyFileProperty.getName()
                , f
                , application.getServerIp()
                , logListProperty
        );
    }

    public void reName(ActionEvent actionEvent) {
        try {
            String newName = MyDialog.showTextInputDialog("输入新的文件名");
            if (newName == null || newName.equals("")) return;
            client.reName(currentPath.getValueSafe() + simpleMyFileProperty.getName()
                    , currentPath.getValueSafe() + newName);
            flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(ActionEvent actionEvent) {
        try {
            if (MyDialog.showCheckAlert("是否确定删除"))
                client.delete(currentPath.getValueSafe() + simpleMyFileProperty.getName());
            flush();
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

    public void upload(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择要上传的文件");
        File f = fileChooser.showOpenDialog(application.getStage());
        if (f == null) return;
        System.out.println(f.getName() + " " + f.length());
        SimpleLogListProperty logListProperty = new SimpleLogListProperty(
                SimpleLogListProperty.TYPE_UPLOAD
                , f.getName()
                , ""
                , f.length()
                , 0);
        logdata.add(logListProperty);
        client.upload(currentPath.getValueSafe(), f, application.getServerIp(), logListProperty);
        flush();
    }

    public void newFileDirectory(ActionEvent actionEvent) throws IOException {
        String text = MyDialog.showTextInputDialog("输入文件夹名称");
        for (int i = 0; i < data.size(); i++) {
            SimpleMyFileProperty tmp = data.get(i);
            if (tmp.getType().equals("文件夹") && tmp.getName().equals(text)) {
                MyDialog.showErrorAlert("名称已存在");
                return;
            }
        }
        client.createFileDirectory(currentPath.getValueSafe(), text);
        flush();
    }

    public void searchFile(ActionEvent actionEvent) throws IOException {
        searchResult.clear();
        MyFile[] myfile = client.searchFile(fileSearchTextField.getText());
        for (MyFile x : myfile) {
            searchResult.add(new SimpleMyFileProperty(x));
        }
    }
}
