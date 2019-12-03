package com.kvrmnks.net;

import com.kvrmnks.data.FileStructure;
import com.kvrmnks.data.MyFile;
import com.kvrmnks.data.SimpleLogListProperty;
import com.kvrmnks.data.SimpleMyFileProperty;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private DataInputStream socketIn;
    private DataOutputStream socketOut;
    private Socket socket;

    public Client(DataInputStream socketIn, DataOutputStream socketOut, Socket socket) {
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.socket = socket;
    }

    public MyFile[] getStructure(String location) throws IOException {
        socketOut.writeUTF("GetStructure" + "$" + location);
        int n = socketIn.readInt();
        MyFile[] ret = new MyFile[n];
        for (int i = 0; i < n; i++) {
            ret[i] = MyFile.constructByStream(socketIn);
        }
        return ret;
    }

    public void downLoad(String vmf, String name, File location, String ip, SimpleLogListProperty simpleLogListProperty) throws IOException {
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
    public void upload(String fileDirectory, String fileName, File localFile, String ip
            , SimpleLogListProperty logListProperty) throws IOException {
        socketOut.writeUTF("UploadFile$" + fileDirectory + "$" + fileName);
        int port = socketIn.readInt();
        Thread t = new Thread(new Uploader(localFile, port, ip, logListProperty));
        t.start();
    }

    /*
     * 不可以指定上传到服务器的文件名的上传
     * */
    public void upload(String fileDirectory, File localFile, String ip, SimpleLogListProperty loglist) throws IOException {
        upload(fileDirectory, localFile.getName(), localFile, ip, loglist);
    }

    public void reName(String file, String newName) throws IOException {
        socketOut.writeUTF("Rename" + "$" + file + "$" + newName);
    }

    public void delete(String s) throws IOException {
        socketOut.writeUTF("Delete$" + s);
    }

    /*
     * fileDirectory 要创建文件夹的目录
     * fileName 要创建的文件夹的名称
     * */
    public void createFileDirectory(String fileDirectory, String fileName) throws IOException {
        socketOut.writeUTF("CreateDirectory$" + fileDirectory + "$" + fileName);
    }

    /*
     * 得到所有的文件结构
     * */
    public void getWholeStructure() throws IOException {
        socketOut.writeUTF("GetWholeStructure");
    }

    /*
     * searchFile 搜索文件
     * fileName 为要搜索的文件子串
     * */
    public MyFile[] searchFile(String fileName) throws IOException {
        getWholeStructure();
        FileStructure fs = new FileStructure();
        fs.receive(socketIn);
        return fs.search(fileName);
    }
}
