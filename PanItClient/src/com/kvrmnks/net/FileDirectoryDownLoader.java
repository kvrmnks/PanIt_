package com.kvrmnks.net;

import com.kvrmnks.data.FileStructure;
import com.kvrmnks.data.MyFile;
import com.kvrmnks.data.SimpleLogListProperty;
import com.kvrmnks.data.SimpleMyFileProperty;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class FileDirectoryDownLoader {
    private String fileLocation, serverIp, panLocation, panFileDirectoryName;
    private DataInputStream socketIn;
    private DataOutputStream socketOut;
    private FileStructure fileStructure;
    private MyFile[] myFiles;
    private SimpleLogListProperty[] simpleLogListProperties;

    public FileDirectoryDownLoader(
            String fileLocation
            , String panLocation
            , String panFileDirectoryName
            , DataInputStream socketIn
            , DataOutputStream socketOut
            , String serverIp) {
        this.fileLocation = fileLocation;
        this.panLocation = panLocation;
        this.panFileDirectoryName = panFileDirectoryName;
        this.socketOut = socketOut;
        this.socketIn = socketIn;
        fileStructure = new FileStructure();
        this.serverIp = serverIp;
    }

    private void getFileList() throws IOException {
        socketOut.writeUTF("GetWholeStructure$" + panLocation + panFileDirectoryName);
        fileStructure.receive(socketIn);
    }

    private void buildFileDirectory() {
        MyFile[] myFiles = fileStructure.getFileDirectory();
        for (MyFile myFile : myFiles) {
            File file = new File(fileLocation + "/" + myFile.getPath());
            if (!file.exists())
                file.mkdirs();
        }
    }

    private void getFileProperty() {
        myFiles = fileStructure.getFile();
        simpleLogListProperties = new SimpleLogListProperty[myFiles.length];
        for (int i = 0; i < myFiles.length; i++) {
            simpleLogListProperties[i] = new SimpleLogListProperty(
                    SimpleLogListProperty.TYPE_DOWNLOAD
                    , myFiles[i].getName()
                    , ""
                    , myFiles[i].getSize()
                    , 0
            );
        }
    }

    public void init() throws IOException {
        getFileList();
        buildFileDirectory();
        getFileProperty();
    }


    public void download() throws IOException {
        for (int i = 0; i < myFiles.length; i++) {
            String filePath = myFiles[i].getPath();
            socketOut.writeUTF("DownloadFile$" + panLocation + filePath);
            int port = socketIn.readInt();
            DownLoader downLoader = new DownLoader(
                    new File(fileLocation + filePath)
                    , port
                    , serverIp
                    , simpleLogListProperties[i]);
            Thread thread = new Thread(downLoader);
            thread.start();
        }
    }

    public SimpleLogListProperty[] getProperty() {
        return simpleLogListProperties;
    }
}
