package com.fanwe.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by didik on 2016/8/23.
 */
public class MGStringFormatter {

    /**
     *
     * @param maybeInt 可能是int类型的值
     * @return error Integer.min_value
     */
    public static int getInt(String maybeInt){
        if(maybeInt == null || maybeInt.equals("")){
            return 0;
        }
        int a=Integer.MIN_VALUE;
        try {
            a= Integer.valueOf(maybeInt);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return a;
    }

    /**
     * 获取 1.0
     * @param maybeFloat
     * @return
     */
    public static String getFloat1(String maybeFloat){
        String result="0.0";
        if (!TextUtils.isEmpty(maybeFloat)){
            int length = maybeFloat.length();
            if (maybeFloat.contains(".")){
                int middle = maybeFloat.indexOf(".");
                int offset = length - middle;
                if (offset==1){
                    result=maybeFloat+"0";
                }else if (offset==2){
                    result=maybeFloat;
                }else if (offset>2){
                    result=maybeFloat.substring(0,middle+2);
                }
            }else {
                result=maybeFloat+".0";
            }
        }

        return result;
    }

    /**
     *
     * @param time
     * @return
     */
    public static String getDate(String time){
        String result="";
        if (TextUtils.isEmpty(time)){
            result="";
        }else {
            Date date=new Date(Long.valueOf(time));
            SimpleDateFormat format=new SimpleDateFormat("yyyy.MM.dd");
            result = format.format(date);
        }
        return result;
    }
}
