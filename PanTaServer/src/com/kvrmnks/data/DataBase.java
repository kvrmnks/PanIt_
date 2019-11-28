package com.kvrmnks.data;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/test_db?useUnicode=true&characterEncoding=UTF-8&userSSL=false&serverTimezone=GMT%2B8"; // 见上面的解释
    private static final String DATABASE_USER = "root"; // 用户名
    //private static final String DATABASE_PASSWORD = "453600@#A";
    private static Connection connection = null;
    private static Statement statement = null;

    static {
        try {
            Class.forName(DATABASE_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void connect(String password) throws Exception {
        connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, password);
        statement = connection.createStatement();
    }

    private static boolean checkUserNameAndPassword(String name, String password) throws SQLException {
        String sql = "SELECT userName,userPassword FROM user where";
        sql += " userName=\"" + name + "\"";
        sql += " AND userPassword=\"" + password + "\"";
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet.next();
    }

    private static boolean checkUserName(String name) throws SQLException {
        String sql = "SELECT userName,userPassword FROM user where";
        sql += " userName=\"" + name + "\"";
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet.next();
    }

    public static boolean has(User user) throws SQLException {
        return checkUserNameAndPassword(user.getName(), user.getPassword());
    }

    public static boolean hasSameName(User user) throws SQLException {
        return checkUserName(user.getName());
    }

    public static void add(User user) throws SQLException {
        String sql = "insert into user (userName,userPassword) values(?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.execute();
    }

    public static User[] getAll() {
        String sql = "SELECT userName,userPassword FROM user";
        ArrayList<User> ret = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                ret.add(new User(resultSet.getString("userName"), resultSet.getString("userPassword")));
            }
            User[] returnUser = new User[ret.size()];
            ret.toArray(returnUser);
            return returnUser;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void modifyPassword(String userName, String newPassword) throws SQLException {
        String sql = "update user set userPassword=? where userName=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //preparedStatement.setString(1,userName);
        preparedStatement.setString(1, newPassword);
        preparedStatement.setString(2, userName);
        preparedStatement.execute();
    }

    public static void deleteUser(String name) throws SQLException {
        String sql = "delete from user where userName = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, name);
        preparedStatement.execute();
    }
}
