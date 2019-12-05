package com.kvrmnks.net;

import com.kvrmnks.UI.MainController;
import com.kvrmnks.data.DataBase;
import com.kvrmnks.data.User;
import com.kvrmnks.data.UserDisk;
import com.kvrmnks.data.UserManager;
import com.kvrmnks.exception.ExceptionSolver;
import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Server implements Runnable {
    private ServerSocket serverSocket;
    private MainController mainController;

    private Server() {
    }

    public Server(ServerSocket serverSocket, MainController mainController) {
        this.serverSocket = serverSocket;
        this.mainController = mainController;
    }


    private void logUp(DataOutputStream socketOut, String s, String s1) {
        try {
            if (DataBase.hasSameName(s)) {
                socketOut.writeBoolean(false);
                return;
            }
            DataBase.add(new User(s, s1));
            UserDisk.initDisk(s);
            socketOut.writeBoolean(true);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
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
                String index = in.readUTF();

                while ((index.split("\\$"))[0].equals("Logup") && index.split("\\$").length == 3) {
                    String[] command = index.split("\\$");
                    logUp(out, command[1], command[2]);
                    Platform.runLater(() -> {
                        mainController.flushUserTable();
                    });

                    index = in.readUTF();
                }


                String[] info = UserManager.getUserNameAndPassword(index);
                while (info.length != 2 || !UserManager.checkUser(info[0], info[1])) {
                    out.writeBoolean(false);
                    info = UserManager.getUserNameAndPassword(in.readUTF());
                }
                out.writeBoolean(true);
                Thread t = new Thread(new Connect(socket, in, out,
                        new User(info[0], info[1])));
                t.start();

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
