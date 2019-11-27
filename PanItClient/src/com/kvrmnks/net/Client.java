package com.kvrmnks.net;

import com.kvrmnks.data.MyFile;
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

    public MyFile[] getStructure(String location) throws IOException{
        socketOut.writeUTF("GetStructure" + "$" + location);
        int n = socketIn.readInt();
        MyFile[] ret = new MyFile[n];
        for (int i = 0; i < n; i++) {
            ret[i] = MyFile.constructByStream(socketIn);
        }
        return ret;
    }

    public void downLoad(String vmf,String name, File location, String ip){
        try {
            socketOut.writeUTF("DownloadFile$"+vmf);
            int port = socketIn.readInt();
            Thread t = new Thread(new DownLoader(new File(location+"/"+name),port,ip));
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void reName(String file,String newName) throws IOException {
        socketOut.writeUTF("Rename"+"$"+file+"$"+newName);
    }

    public void delete(String s) throws IOException {
        socketOut.writeUTF("Delete$"+s);
    }
}
