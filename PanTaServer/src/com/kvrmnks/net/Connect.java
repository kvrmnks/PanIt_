package com.kvrmnks.net;

import com.kvrmnks.UI.MainController;
import com.kvrmnks.data.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;

public class Connect implements Runnable {
    private Socket socket;
    private DataInputStream socketIn;
    private DataOutputStream socketOut;
    private MainController mainController;
    private User user;

    public Connect(Socket socket, DataInputStream socketIn, DataOutputStream socketOut
            , MainController mainController, User user) {
        this.socket = socket;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.mainController = mainController;
        this.user = user;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getSocketIn() {
        return socketIn;
    }

    public void setSocketIn(DataInputStream socketIn) {
        this.socketIn = socketIn;
    }

    public DataOutputStream getSocketOut() {
        return socketOut;
    }

    public void setSocketOut(DataOutputStream socketOut) {
        this.socketOut = socketOut;
    }


    private void download(String file) {
        Thread t = new Thread(new Downloader(socket, socketIn, socketOut,
                UserDisk.getDiskLocation(user.getName()) + file));
        t.start();
    }

    private void upload(String fileto, String file) {
        Thread t = new Thread(new Uploader(socket, socketIn, socketOut, fileto, file));
        t.start();
    }

    //暂行
    private void getStructure() {

    }

    private void getStructure(String location) {
        location = UserDisk.getDiskLocation(user.getName()) + location;
        try {
            socketOut.writeInt(FileManager.countAllByLocation(location));
            for (File f : FileManager.getFileDirectoryByLocation(location)) {
                new MyFile(f.getName(), f.length(), MyFile.TYPEFILEDERECTORY
                        , MyDate.convert("" + f.lastModified())
                ).writeByStream(socketOut);
            }

            for (File f : FileManager.getFileByLocation(location)) {
                new MyFile(f.getName(), f.length(), MyFile.TYPEFILE
                        , MyDate.convert("" + f.lastModified())
                ).writeByStream(socketOut);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void rename(String file, String newName) {
        File f = new File(UserDisk.getDiskLocation(user.getName()) + file);
        f.renameTo(new File(UserDisk.getDiskLocation(user.getName()) + newName));
    }

    private void delete(String file) {
        File f = new File(UserDisk.getDiskLocation(user.getName()) + file);
        if (!f.delete()) {
            System.out.println("failed");
        }
    }

    private void doCommands() throws IOException {
        String[] command = socketIn.readUTF().split("\\$");
        switch (command[0]) {
            case "GetStructure":
                if (command.length > 1) {
                    getStructure(command[1]);
                } else {
                    ;
                }
                break;
            case "UploadFile":
                upload(command[1], command[2]);
                break;
            case "CreateDirectory":
                break;
            case "DownloadFile":
                download(command[1]);
                break;
            case "Rename":
                rename(command[1], command[2]);
                break;
            case "Delete":
                delete(command[1]);
                break;
            case "Display":
                getStructure();
                break;

        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                doCommands();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
