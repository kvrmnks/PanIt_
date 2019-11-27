package com.kvrmnks.data;

public class UserDisk {
    private final static String LOCATION = "d:/";
    private String diskLocation;
    private User user;

    public static String getDiskLocation(String name){
        return LOCATION + name;
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
