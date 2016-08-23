package com.fanwe.utils;

/**
 * Created by didik on 2016/8/23.
 */
public class MGString2Num {

    public static int getInt(String maybeInt){
        int a=Integer.MIN_VALUE;
        try {
            a= Integer.valueOf(maybeInt);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return a;
    }
}
