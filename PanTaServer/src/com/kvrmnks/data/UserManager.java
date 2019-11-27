package com.kvrmnks.data;

import java.io.*;

public class UserManager implements Serializable {


    private DataBase dataBase = new DataBase();

    public UserManager() {

    }

    private void save() {
        dataBase.save();
    }

    public static String[] getUserNameAndPassword(String str) {
        return str.split("\\$");
    }

    public boolean checkUser(String userName, String password) {
        return dataBase.has(new User(userName, password));
    }

    public boolean checkUserByName(String userName){
        return dataBase.hasSameName(new User(userName,""));
    }

    public void addUser(String userName, String password){
        dataBase.add(new User(userName,password));
    }
}
