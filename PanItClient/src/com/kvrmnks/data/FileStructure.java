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

    private void buildPath() {
        MyFile MF[] = new MyFile[myfile.size()];
        myfile.toArray(MF);
        Arrays.sort(MF, Comparator.comparingInt(MyFile::getId));
        for (MyFile myFile : MF) {
            MyFile tmpFile = myFile;
            ArrayList<Integer> arrayList = new ArrayList<>();
            System.out.println(myFile);
            arrayList.add(tmpFile.getId());
            while (tmpFile.getFatherId() != 0) {
                tmpFile = MF[tmpFile.getFatherId() - 1];
                arrayList.add(tmpFile.getId());
            }
            StringBuilder sb = new StringBuilder();
            for (int i = arrayList.size() - 1; i >= 0; i--) {
                sb.append("/").append(MF[arrayList.get(i) - 1].getName());
            }
            myFile.setPath(sb.toString());
        }
        myfile = new ArrayList<MyFile>();
        Collections.addAll(myfile, MF);
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
        buildPath();
    }

    public MyFile[] search(String name) {
        MyFile MF[] = new MyFile[myfile.size()];
        myfile.toArray(MF);
        ArrayList<MyFile> mf = new ArrayList<>();
        for (MyFile file : MF) {
            if (file.getName().contains(name))
                mf.add(file);
        }
        MyFile[] _MF = new MyFile[mf.size()];
        mf.toArray(_MF);
        return _MF;
    }

    private MyFile[] getFileByType(int type) {
        int cnt = 0;
        for (MyFile mf : myfile)
            if (mf.getType() == type)
                cnt++;
        MyFile[] myFiles = new MyFile[cnt];
        cnt = 0;
        for (MyFile mf : myfile)
            if (mf.getType() == type)
                myFiles[cnt++] = mf;
        return myFiles;
    }

    public MyFile[] getFileDirectory() {
        return getFileByType(MyFile.TYPEFILEDERECTORY);
    }

    public MyFile[] getFile() {
        return getFileByType(MyFile.TYPEFILE);
    }

    public void setMyfile(ArrayList<MyFile> list) {
        myfile = list;
        buildPath();
    }
}
