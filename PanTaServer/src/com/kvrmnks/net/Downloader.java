package com.kvrmnks.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Downloader implements Runnable {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Socket realSocket;
    private int port;
    private String file;
    private ServerSocket serverSocket;

    private Downloader() {
    }

    public Downloader(Socket socket, DataInputStream in, DataOutputStream out, String file) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.file = file;
    }

    private ServerSocket findEmptyServerSocket() throws IOException {
        ServerSocket ss = null;
        ss = new ServerSocket(0);
        return ss;

    }

    private void setConnect() {
        try {
            serverSocket = findEmptyServerSocket();
            port = serverSocket.getLocalPort();
            out.writeInt(serverSocket.getLocalPort());
            realSocket = serverSocket.accept();
            in = new DataInputStream(realSocket.getInputStream());
            out = new DataOutputStream(realSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mainWork() throws IOException {
        File f = new File(file);
        DataInputStream fileIn = new DataInputStream(new FileInputStream(f));
        long length = f.length();
        out.writeLong(length);
        byte[] buffer = new byte[1024];
        long current = 0;
        while (current < length) {
            int w = fileIn.read(buffer);
            current += w;
            out.write(buffer, 0, w);
        }
        fileIn.close();
    }

    @Override
    public void run() {
        setConnect();
        try {
            mainWork();
            serverSocket.close();
            realSocket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}