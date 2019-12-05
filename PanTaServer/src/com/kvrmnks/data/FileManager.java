package com.kvrmnks.data;

import java.io.File;

public class FileManager {
    public static File[] getFileByLocation(String location) {
        File[] list = new File(location).listFiles();
        int n = 0;
        File[] ret;
        if (list == null) return null;
        for (File f : list) if (f.isFile()) n++;
        ret = new File[n];
        n = 0;
        for (File f : list) if (f.isFile()) ret[n++] = f;
        return ret;
    }

    public static File[] getFileDirectoryByLocation(String location) {
        File[] list = new File(location).listFiles();
        int n = 0;
        File[] ret;
        if (list == null) return null;
        for (File f : list) if (f.isDirectory()) n++;
        ret = new File[n];
        n = 0;
        for (File f : list) if (f.isDirectory()) ret[n++] = f;
        return ret;
    }

    public static int countFileByLocation(String location) {
        File[] files = getFileByLocation(location);
        if (files == null) return 0;
        return files.length;
    }

    public static int countFileDirectoryByLocation(String location) {
        File[] files = getFileDirectoryByLocation(location);
        if (files == null) return 0;
        return files.length;
    }

    public static int countAllByLocation(String location) {
        return countFileByLocation(location) + countFileDirectoryByLocation(location);
    }
}
