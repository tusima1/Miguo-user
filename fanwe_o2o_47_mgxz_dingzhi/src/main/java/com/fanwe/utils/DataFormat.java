package com.fanwe.utils;

/**
 * Created by Administrator on 2016/8/23.
 */
public class DataFormat {

    public static float toFloat(String str) {
        try {
            return Float.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double toDouble(String str) {
        try {
            return Double.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int toInt(String str) {
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long toLong(String str) {
        try {
            return Long.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean toBoolean(String str) {
        try {
            return Boolean.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    static java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

    public static String toDoubleTwo(String str) {
        String res = df.format(toDouble(str));
        if (res.startsWith(".")) {
            res = "0" + res;
        }
        return res;
    }
    public static String toDoubleTwo(float value){
        String res = df.format(value);
        if (res.startsWith(".")) {
            res = "0" + res;
        }
        return res;
    }
}
