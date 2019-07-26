package com.Lowser.sharefile.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GenerateNum {
    private static final String format = "yyyyMMddHHmmssSSS";
    public static String getNum() {
        String dt = new SimpleDateFormat(format).format(new Date());

        return dt + getRandom(2);
    }
    public static String getRandom(int length) {
        String[] digits = {"1","2","3","4","5","6","7","8","9","0"};
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i =0; i< length ; i++) {
            String randomString = digits[random.nextInt(digits.length)];
            builder.append(randomString);
        }
        return builder.toString();
    }
}
