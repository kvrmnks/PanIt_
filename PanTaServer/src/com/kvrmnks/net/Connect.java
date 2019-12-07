package com.kvrmnks.net;

import com.kvrmnks.UI.MainController;
import com.kvrmnks.data.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.sql.SQLException;

public class Connect implements Runnable {
    private Socket socket;
    private DataInputStream socketIn;
    private DataOutputStream socketOut;
    private User user;

    public Connect(Socket socket, DataInputStream socketIn, DataOutputStream socketOut
            , User user) {
        this.socket = socket;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
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
        Thread t = new Thread(new Uploader(socket, socketIn, socketOut,
                UserDisk.getDiskLocation(user.getName()) + fileto, file));
        t.start();
    }

    //暂行
    private void getStructure() {

    }

    private void getStructure(String location) {
        location = UserDisk.getDiskLocation(user.getName()) + location;
        try {
            socketOut.writeInt(FileManager.countAllByLocation(location));
            File[] files = FileManager.getFileDirectoryByLocation(location);
            if (files != null)
                for (File f : files) {
                    new MyFile(f.getName(), f.length(), MyFile.TYPEFILEDERECTORY
                            , MyDate.convert("" + f.lastModified())
                    ).writeByStream(socketOut);
                }
            files = FileManager.getFileByLocation(location);
            if (files != null)
                for (File f : files) {
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

    private void createDirectory(String s, String s1) {
        File f = new File(UserDisk.getDiskLocation(user.getName()) + s + "/" + s1);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    private void createDirectory(String s) {
        File f = new File(UserDisk.getDiskLocation(user.getName()) + "/" + s);
        f.mkdirs();
    }


    private void getWholeStructure(String s) throws IOException {
        IOFile ioFile = new IOFile();
        ioFile.input(UserDisk.getDiskLocation(user.getName() + "/" + s));
        FileStructure fileStructure = new FileStructure();
        fileStructure.setMyfile(ioFile.getList());
        fileStructure.send(socketOut);
    }

    private void getWholeStructure() throws IOException {
        IOFile ioFile = new IOFile();
        ioFile.input(UserDisk.getDiskLocation(user.getName()));
        FileStructure fileStructure = new FileStructure();
        fileStructure.setMyfile(ioFile.getList());
        fileStructure.send(socketOut);
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
                if (command.length > 2)
                    createDirectory(command[1], command[2]);
                else
                    createDirectory(command[1]);
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
            case "GetWholeStructure":
                if (command.length == 1)
                    getWholeStructure();
                else
                    getWholeStructure(command[1]);
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
