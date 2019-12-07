package com.kvrmnks.data;

import java.io.File;
import java.util.ArrayList;

import static com.kvrmnks.data.MyFile.TYPEFILE;
import static com.kvrmnks.data.MyFile.TYPEFILEDERECTORY;

public class IOFile {
    private ArrayList<MyFile> list = new ArrayList<MyFile>();
    private int id = 0;

    private void analyse(File x, int id) {
        File[] files = x.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                MyFile mf = new MyFile(
                        file.getName()
                        , file.length()
                        , TYPEFILEDERECTORY
                        , MyDate.convert("" + file.lastModified())
                );
                mf.setFatherId(id);
                mf.setId(++this.id);
                analyse(file, this.id);
                list.add(mf);
            } else {
                MyFile mf = new MyFile(
                        file.getName()
                        , file.length()
                        , TYPEFILE
                        , MyDate.convert("" + file.lastModified())
                );
                mf.setId(++this.id);
                mf.setFatherId(id);
                list.add(mf);
            }

        }
    }

    public void input(String location) {
        File rootFile = new File(location);
        MyFile mf = new MyFile(rootFile.getName()
                , rootFile.length()
                , TYPEFILEDERECTORY
                , MyDate.convert("" + rootFile.lastModified()));
        mf.setId(++this.id);
        mf.setFatherId(0);
        list.add(mf);
        analyse(rootFile, this.id);
    }

    public ArrayList<MyFile> getList() {
        return list;
    }

}
