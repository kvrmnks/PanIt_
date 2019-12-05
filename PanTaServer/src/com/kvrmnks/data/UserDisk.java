package com.kvrmnks.data;

import java.io.File;
import java.io.IOException;

public class UserDisk {
    private final static String LOCATION = "c:/";
    private String diskLocation;
    private User user;

    public static String getDiskLocation(String name) {
        return LOCATION + name;
    }

    public static void initDisk(String name) {
        File file = new File(LOCATION + name);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private UserDisk() {
    }

    public UserDisk(User user) {
        this.user = user;
        diskLocation = LOCATION + user.getName();
    }

    public UserDisk(String name) {
        diskLocation = LOCATION + name;
    }

    public String getDiskLocation() {
        return diskLocation;
    }

}
