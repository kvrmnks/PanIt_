package com.kvrmnks.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class MyFile implements Serializable {
    private String name, modifyTime, path;
    private Long size;
    private int type, id, fatherId;
    public static final int TYPEFILE = 0;
    public static final int TYPEFILEDERECTORY = 1;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static String backFroward(String str) {
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        for (int i = 0; i < str.length(); i++) if (str.charAt(i) == '/') cnt++;
        if (cnt == 1)
            return str;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '/') cnt--;
            sb.append(str.charAt(i));
            if (cnt == 1) break;

        }
        return sb.toString();
    }

    public MyFile() {
    }

    public MyFile(String name, Long size, int type, String modifyTime) {
        this.name = name;
        this.modifyTime = modifyTime;
        this.size = size;
        this.type = type;
    }

    public MyFile(String name, String modifyTime, Long size, int type, int id, int fatherId) {
        this.name = name;
        this.modifyTime = modifyTime;
        this.size = size;
        this.type = type;
        this.id = id;
        this.fatherId = fatherId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFatherId() {
        return fatherId;
    }

    public void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static int getTYPEFILE() {
        return TYPEFILE;
    }

    public static int getTYPEFILEDERECTORY() {
        return TYPEFILEDERECTORY;
    }

    public static MyFile constructByStream(DataInputStream in) throws IOException {
        MyFile mf = null;
        mf = new MyFile(in.readUTF(), in.readLong(), in.readInt(), in.readUTF());
        return mf;
    }

    public void writeByStream(DataOutputStream out) throws IOException {
        out.writeUTF(name);
        out.writeLong(size);
        out.writeInt(type);
        out.writeUTF(modifyTime);
    }

    @Override
    public String toString() {
        return ("name:" + name + " modifyTime:" + modifyTime + " path:" + path + " size:" + size + " type:" + type
                + " id:" + id + " fatherId:" + fatherId);
    }
}
