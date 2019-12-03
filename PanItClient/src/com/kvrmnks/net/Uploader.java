package com.kvrmnks.net;

import com.kvrmnks.data.SimpleLogListProperty;
import com.kvrmnks.exception.ExceptionSolver;

import java.io.*;
import java.net.Socket;

public class Uploader implements Runnable {
    private final static int BUFFERSIZE = 1024;
    private String localFilePath;
    private File localFile;
    private Socket socket;
    private DataInputStream socketIn, fileIn;
    private DataOutputStream socketOut;
    private SimpleLogListProperty logListProperty;

    private Uploader() {
    }

    /*
     * localFilePath 要上传的文件地址
     * port 提供上传的端口
     * ip 提供上传的服务器
     * */
    public Uploader(String localFilePath
            , int port, String ip, SimpleLogListProperty logListProperty) throws IOException {
        this.localFilePath = localFilePath;
        localFile = new File(localFilePath);
        socket = new Socket(ip, port);
        this.logListProperty = logListProperty;
    }

    public Uploader(File localFile
            , int port, String ip, SimpleLogListProperty logListProperty) throws IOException {
        this.localFile = localFile;
        this.localFilePath = localFile.getName();
        socket = new Socket(ip, port);
        this.logListProperty = logListProperty;
    }

    private void setConnect() throws IOException {
        socketIn = new DataInputStream(socket.getInputStream());
        socketOut = new DataOutputStream(socket.getOutputStream());
        fileIn = new DataInputStream(new FileInputStream(localFile));
    }

    private void upload() throws IOException {
        long len = localFile.length();
        socketOut.writeLong(len);
        byte[] buffer = new byte[BUFFERSIZE];
        long current = 0;
        while (current < len) {
            int tmp = fileIn.read(buffer);
            socketOut.write(buffer, 0, tmp);
            current += tmp;
            double cur_prac = ((double) current) / len;
            if (cur_prac - 0.01 > logListProperty.getProcess())
                logListProperty.setProcess((cur_prac));
            //System.out.println(((double)current)/len +"\n" + logListProperty.getProcess());
        }
        logListProperty.setProcess(1);
    }


    @Override
    public void run() {
        try {
            setConnect();
            upload();
            socket.close();
            socketIn.close();
            socketOut.close();
            fileIn.close();
        } catch (IOException e) {
            ExceptionSolver.solve(e);
        }

    }
}
