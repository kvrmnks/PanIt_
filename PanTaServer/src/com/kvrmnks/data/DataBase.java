package com.kvrmnks.data;

import java.io.*;
import java.util.ArrayList;

public class DataBase implements Serializable {
    private final String dataLocation = "data.dat";
    ArrayList<User> user = new ArrayList<>();

    void readObject() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(dataLocation)));
            user = (ArrayList<User>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            //e.printStackTrace();
        }

    }

    void writeObject() {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(dataLocation)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            objectOutputStream.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    DataBase() {
        readObject();
    }

    public void save() {
        writeObject();
    }

    public void add(User a) {
        user.add(a);
    }

    public boolean hasSameName(User a) {
        for (User u : user) {
            if (u.getName().equals(a.getName()))
                return true;
        }
        return false;
    }

    public boolean has(User a) {
        for (int j = 0; j < user.size(); j++) {
            if (a.getName().equals(user.get(j).getName()) && a.getPassword().equals(user.get(j).getPassword()))
                return true;
        }
        return false;
    }

    public User find(User a) {
        return a;
    }

}
