package com.kvrmnks.data;

public class MyDate {
    public static String convert(String timestampString) {
        Long timestamp = Long.parseLong(timestampString);
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
        return date;
    }

}
