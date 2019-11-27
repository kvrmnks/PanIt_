package com.kvrmnks.data;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String getMD5(String text) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("MD5").digest(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String md5 = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5.length(); i++)
            md5 = "0" + md5;
        return md5;
    }
}
