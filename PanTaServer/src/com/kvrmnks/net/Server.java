package com.kvrmnks.net;

import com.kvrmnks.UI.MainController;
import com.kvrmnks.data.User;
import com.kvrmnks.data.UserManager;
import com.kvrmnks.exception.ExceptionSolver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Server implements Runnable {
    private ServerSocket serverSocket;

    private Server() {
    }

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        // this.mainController = mainController;
    }


    /* public MainController getMainController() {
         return mainController;
     }

     public void setMainController(MainController mainController) {
         this.mainController = mainController;
     }
     */
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
                ExceptionSolver.solve(e);
            }
        }

    }
}
