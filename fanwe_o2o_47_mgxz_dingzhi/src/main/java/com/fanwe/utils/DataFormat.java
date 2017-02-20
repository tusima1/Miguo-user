package com.fanwe.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

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

    /**
     * 传入double，返回两位小数double,四舍五入
     * @param d
     * @return
     */
    public static Double toDouble(Double d){
        BigDecimal bigDecimal = new BigDecimal(d);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 传入double，返回两位小数double,四舍五入
     * @param d
     * @return
     */
    public static Double toDoubleDown(Double d){
        BigDecimal bigDecimal = new BigDecimal(d);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    /**
     * 判断double是否是整数
     * @param obj
     * @return
     */
    public static boolean isIntegerForDouble(double obj) {
        double eps = 1e-10;  // 精度范围
        return obj-Math.floor(obj) < eps;
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
    public static String toDoubleTwo(double value){
        String res = df.format(value);
        if (res.startsWith(".")) {
            res = "0" + res;
        }
        return res;
    }
}
