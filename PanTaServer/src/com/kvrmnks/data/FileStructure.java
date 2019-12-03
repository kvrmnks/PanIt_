package com.kvrmnks.data;

import java.io.*;
import java.util.*;

public class FileStructure {
    private ArrayList<MyFile> myfile;

    public FileStructure() {
        myfile = new ArrayList<>();
    }

    public void send(DataOutputStream out) throws IOException {
        out.writeInt(myfile.size());
        for (int i = 0; i < myfile.size(); i++) {
            out.writeUTF(myfile.get(i).getName());
            out.writeLong(myfile.get(i).getSize());
            out.writeInt(myfile.get(i).getType());
            out.writeUTF(myfile.get(i).getModifyTime());
            out.writeInt(myfile.get(i).getId());
            out.writeInt(myfile.get(i).getFatherId());
        }
    }

    public void receive(DataInputStream in) throws IOException {

        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            MyFile mf = new MyFile();
            mf.setName(in.readUTF());
            mf.setSize(in.readLong());
            mf.setType(in.readInt());
            mf.setModifyTime(in.readUTF());
            mf.setId(in.readInt());
            mf.setFatherId(in.readInt());
            myfile.add(mf);
        }
    }

    public MyFile[] search(String name) {
        MyFile MF[] = new MyFile[myfile.size()];
        myfile.toArray(MF);
        Arrays.sort(MF, Comparator.comparingInt(a -> a.id));
        for (MyFile myFile : MF) {
            MyFile tmpFile = myFile;
            ArrayList<Integer> arrayList = new ArrayList<>();
            System.out.println(myFile);
            arrayList.add(tmpFile.id);
            while (tmpFile.fatherId != 0) {
                tmpFile = MF[tmpFile.fatherId - 1];
                arrayList.add(tmpFile.id);
            }
            StringBuilder sb = new StringBuilder();
            for (int i = arrayList.size() - 1; i >= 0; i--) {
                sb.append("/").append(MF[arrayList.get(i) - 1].name);
            }
            myFile.path = sb.toString();
        }
        ArrayList<MyFile> mf = new ArrayList<>();
        for (MyFile file : MF) {
            if (file.getName().contains(name))
                mf.add(file);
        }
        MyFile[] _MF = new MyFile[mf.size()];
        mf.toArray(_MF);
        return _MF;
    }

    public void setMyfile(ArrayList<MyFile> list) {
        myfile = list;
    }
}
