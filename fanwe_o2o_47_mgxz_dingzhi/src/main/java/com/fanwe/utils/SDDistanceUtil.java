package com.fanwe.utils;

/**
 * @author didik
 * time : 2016/10/31
 * 1-999m 显示单位m ,最小1米 ;1km-99.9km 显示单位km ，最小0.1km ;大于等于100km ,显示 >100km
 */
public class SDDistanceUtil {

    public static String getMGDistance(double distance) {
        String result;
        if (distance <= 0) {
            result = "";
        } else if (distance > 0 && distance < 1000) {
            result = ((int) distance) + "m";
        } else if (1000 <= distance && distance <= 99900) {
            result = MGStringFormatter.getFloat1(((float) distance / 1000) + "") + "km";
        } else {
            result = ">100km";
        }
        return result;
    }

    public static String getFormatDistance(double distance) {
        return getMGDistance(distance);
    }

    public static String getKmDistanceString(int distance) {
        return getMGDistance(distance);
    }

    public static String getKmDistanceString(double distance) {
        return getMGDistance(distance);
    }
}
