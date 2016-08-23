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
}
