package com.kvrmnks.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Uploader implements Runnable {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Socket realSocket;
    private int port;
    private String fileto, file;

    private Uploader() {
    }

    public Uploader(Socket socket, DataInputStream in, DataOutputStream out, String fileto, String file) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.file = file;
        this.fileto = fileto;
    }

    private ServerSocket findEmptyServerSocket() throws IOException {
        ServerSocket ss = new ServerSocket(0);
        return ss;
    }

    private void setConnect() {
        //port = findEmptyPort();
        try {
            ServerSocket serverSocket = findEmptyServerSocket();
            port = serverSocket.getLocalPort();
            out.writeInt(port);
            realSocket = serverSocket.accept();
            in = new DataInputStream(realSocket.getInputStream());
            out = new DataOutputStream(realSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mainWork() throws IOException {
        File f = new File(fileto + "/" + file);
        DataOutputStream fileOut = new DataOutputStream(new FileOutputStream(f));
        long length = in.readLong();
        long current = 0;
        byte[] buffer = new byte[1024];
        while (current < length) {
            int w = in.read(buffer);
            fileOut.write(buffer, 0, w);
            current += w;
        }
        fileOut.close();
    }

    @Override
    public void run() {
        setConnect();
        try {
            mainWork();
            realSocket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
