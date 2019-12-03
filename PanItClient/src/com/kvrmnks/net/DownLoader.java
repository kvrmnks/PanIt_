package com.kvrmnks.net;

import com.kvrmnks.data.SimpleLogListProperty;
import com.kvrmnks.exception.ExceptionSolver;

import java.io.*;
import java.net.Socket;

public class DownLoader implements Runnable {
    private final static int BUFFERSIZE = 1024;
    private File file;
    private int port;
    private String ip;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream, fileOutputStream;
    private SimpleLogListProperty logListProperty;

    private DownLoader() {
    }

    public DownLoader(File file, int port, String ip, SimpleLogListProperty simpleLogListProperty) throws IOException {
        if (file.exists())
            file.delete();
        file.createNewFile();
        System.out.println(file.getAbsolutePath() + " " + ip + " " + port);
        socket = new Socket(ip, port);
        this.ip = ip;
        this.file = file;
        this.port = port;
        this.logListProperty = simpleLogListProperty;
    }

    private void setConnect() throws IOException {
        //socket = new Socket();
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        fileOutputStream = new DataOutputStream(new FileOutputStream(file));
    }


    private void downLoad() throws IOException {
        long len = dataInputStream.readLong();
        long current = 0;
        byte[] buffer = new byte[BUFFERSIZE];
        while (current < len) {
            int tmp = dataInputStream.read(buffer);
            current += tmp;
            fileOutputStream.write(buffer, 0, tmp);
            double cur_prac = ((double) current) / len;
            if (cur_prac - 0.01 > logListProperty.getProcess())
                logListProperty.setProcess((cur_prac));
        }
        logListProperty.setProcess(1);
        fileOutputStream.close();
    }

    @Override
    public void run() {
        try {
            setConnect();
            downLoad();

            dataInputStream.close();
            dataOutputStream.close();
            fileOutputStream.close();
            socket.close();
            System.gc();
            // System.out.println("2332");
        } catch (IOException e) {
            ExceptionSolver.solve(e);
        }
    }
}
