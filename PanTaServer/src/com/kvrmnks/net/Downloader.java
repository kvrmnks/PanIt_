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

    private int findEmptyPort() {
        for (int i = 8889; true; i++) {
            try {
                Socket s = new Socket("localhost", i);
                s.setSoTimeout(5);
                s.close();
            } catch (IOException e) {
                return i;
            }
        }
    }

    private void setConnect() {
        port = findEmptyPort();
        try {
            serverSocket = new ServerSocket(port);
            out.writeInt(port);
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