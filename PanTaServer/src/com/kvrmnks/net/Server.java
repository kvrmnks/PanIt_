package com.kvrmnks.net;

import com.kvrmnks.UI.MainController;
import com.kvrmnks.data.User;
import com.kvrmnks.data.UserManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private ServerSocket serverSocket;
    private UserManager userManager;// = new UserManager();
    private MainController mainController;

    private Server() {
    }

    public Server(ServerSocket serverSocket, MainController mainController) {
        this.serverSocket = serverSocket;
        this.mainController = mainController;
    }


    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {

        while (true) {
            Socket socket;
            DataInputStream in;
            DataOutputStream out;
            try {
                System.out.println("Waiting");
                socket = serverSocket.accept();
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                String[] info = UserManager.getUserNameAndPassword(in.readUTF());
                while (info.length != 2 || !userManager.checkUser(info[0], info[1])) {
                    out.writeBoolean(false);
                    info = UserManager.getUserNameAndPassword(in.readUTF());
                }
                out.writeBoolean(true);
                Thread t = new Thread(new Connect(socket, in, out, mainController,
                        new User(info[0],info[1])));
                t.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
