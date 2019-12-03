package com.kvrmnks.data;

import java.io.*;
import java.util.*;

import static com.kvrmnks.data.MyFile.TYPEFILE;
import static com.kvrmnks.data.MyFile.TYPEFILEDERECTORY;

public class IOFile {
    private static ArrayList<MyFile> list = new ArrayList<MyFile>();
    private int id = 1, fatherId = 0;


    private void input(String location) {
        int type, ID, FATHERID;
        File file = new File(location);
        if (file.isDirectory())
            type = TYPEFILEDERECTORY;
        else type = TYPEFILE;
        ID = id;
        FATHERID = fatherId;
        list.add(new MyFile(file.getName(), String.valueOf(file.lastModified()), file.length(), type, ID, FATHERID));
        fatherId = ID;
        File[] file1 = file.listFiles();
        for (File value : file1) {
            id++;
            input(value.getPath());
        }
    }

    public static ArrayList<MyFile> getList() {
        return list;
    }

}
