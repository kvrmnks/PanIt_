package com.kvrmnks.net;

import com.kvrmnks.data.FileStructure;
import com.kvrmnks.data.MyFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class FileDirectoryDownLoader implements Runnable{
    private String fileLocation,serverIp;
    private int port;
    private DataInputStream socketIn;
    private DataOutputStream socketOut;
    private Socket socket;
    private FileStructure fileStructure;

    public FileDirectoryDownLoader(String fileLocation,String serverIp,int port) throws IOException {
        this.fileLocation = fileLocation;
        this.serverIp = serverIp;
        this.port = port;
        socket = new Socket(serverIp,port);
        socketOut = new DataOutputStream(socket.getOutputStream());
        socketIn = new DataInputStream(socket.getInputStream());
        fileStructure = new FileStructure();
    }

    private void getFileList() throws IOException {
        fileStructure.receive(socketIn);
    }

    private void buildFileDirectory(){
        MyFile[] myFiles = fileStructure.getFileDirectory();
        for(MyFile myFile : myFiles){
            File file = new File(fileLocation + myFile.getPath());
            if(!file.exists())
                file.mkdirs();
        }
    }

    private void downLoadFile(){
        MyFile[] myFiles = fileStructure.getFile();

    }

    @Override
    public void run() {
        try {
            getFileList();
            buildFileDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
