package com.kvrmnks.data;

import java.io.*;
import java.sql.SQLException;

public class UserManager implements Serializable {


    //private DataBase dataBase = new DataBase();

    public UserManager() {
    }

    public static String[] getUserNameAndPassword(String str) {
        return str.split("\\$");
    }

    public static boolean checkUser(String userName, String password) throws SQLException {
        return DataBase.has(new User(userName, password));
    }

    public static boolean checkUserByName(String userName) throws SQLException {
        return DataBase.hasSameName(new User(userName, ""));
    }

    public static void addUser(String userName, String password) throws SQLException {
        DataBase.add(new User(userName, password));
    }

    /*保证用户存在*/
    public static void modifyPassword(String userName, String newPassword) throws SQLException {
        DataBase.modifyPassword(userName, newPassword);
    }

    /*保证用户存在*/
    public static void deleteUser(String name) throws SQLException {
        DataBase.deleteUser(name);
    }
}
