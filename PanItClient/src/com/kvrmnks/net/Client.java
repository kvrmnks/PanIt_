package com.kvrmnks.net;

import com.kvrmnks.data.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private static DataInputStream socketIn;
    private static DataOutputStream socketOut;
    private static Socket socket;
    private static String serverIp;

    public static DataInputStream getSocketIn() {
        return socketIn;
    }

    public static void setSocketIn(DataInputStream socketIn) {
        Client.socketIn = socketIn;
    }

    public static DataOutputStream getSocketOut() {
        return socketOut;
    }

    public static void setSocketOut(DataOutputStream socketOut) {
        Client.socketOut = socketOut;
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        Client.socket = socket;
    }

    public Client(DataInputStream socketIn, DataOutputStream socketOut, Socket socket) {
        Client.socketIn = socketIn;
        Client.socketOut = socketOut;
        Client.socket = socket;
    }

    public static void setClient(DataInputStream socketIn, DataOutputStream socketOut, Socket socket) {
        Client.socketIn = socketIn;
        Client.socketOut = socketOut;
        Client.socket = socket;
    }

    public static String getServerIp() {
        return serverIp;
    }

    public static void setServerIp(String serverIp) {
        Client.serverIp = serverIp;
    }

    public static MyFile[] getStructure(String location) throws IOException {
        socketOut.writeUTF("GetStructure" + "$" + location);
        int n = socketIn.readInt();
        MyFile[] ret = new MyFile[n];
        for (int i = 0; i < n; i++) {
            ret[i] = MyFile.constructByStream(socketIn);
        }
        return ret;
    }

    public static void downLoad(String vmf, String name, File location, String ip, SimpleLogListProperty simpleLogListProperty) throws IOException {
        socketOut.writeUTF("DownloadFile$" + vmf);
        int port = socketIn.readInt();
        Thread t = new Thread(new DownLoader(new File(location + "/" + name), port, ip, simpleLogListProperty));
        t.start();
    }

    /*
     * 可以指定上传到服务器的文件名的上传
     * fileDirectory 上传到服务器的文件路径
     * fileName 上传到服务器的文件名
     * localFile 待上传的本地文件
     * ip 服务器的地址
     * */
    public static void upload(String fileDirectory, String fileName, File localFile, String ip
            , SimpleLogListProperty logListProperty) throws IOException {
        socketOut.writeUTF("UploadFile$" + fileDirectory + "$" + fileName);
        int port = socketIn.readInt();
        Thread t = new Thread(new Uploader(localFile, port, ip, logListProperty));
        t.start();
    }

    /*
     * 不可以指定上传到服务器的文件名的上传
     * */
    public static void upload(String fileDirectory, File localFile, String ip, SimpleLogListProperty loglist) throws IOException {
        upload(fileDirectory, localFile.getName(), localFile, ip, loglist);
    }

    public static void reName(String file, String newName) throws IOException {
        socketOut.writeUTF("Rename" + "$" + file + "$" + newName);
    }

    public static void delete(String s) throws IOException {
        socketOut.writeUTF("Delete$" + s);
    }

    /*
     * fileDirectory 要创建文件夹的目录
     * fileName 要创建的文件夹的名称
     * */
    public static void createFileDirectory(String fileDirectory, String fileName) throws IOException {
        socketOut.writeUTF("CreateDirectory$" + fileDirectory + "$" + fileName);
    }

    /*
     * 得到所有的文件结构
     * */
    public static void getWholeStructure() throws IOException {
        socketOut.writeUTF("GetWholeStructure");
    }

    /*
     * searchFile 搜索文件
     * fileName 为要搜索的文件子串
     * */
    public static MyFile[] searchFile(String fileName) throws IOException {
        getWholeStructure();
        FileStructure fs = new FileStructure();
        fs.receive(socketIn);
        return fs.search(fileName);
    }

    /*
     * logup 登录
     * name 用户名
     * password 密码
     * 会自动把输入的密码经过MD5加密之后发送
     * */
    public static boolean logup(String name, String password) throws IOException {
        String passwordMD5 = MD5.getMD5(password);
        socketOut.writeUTF("Logup$" + name + "$" + passwordMD5);
        return socketIn.readBoolean();
    }
}
