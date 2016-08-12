package com.fanwe.seller.presenters;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/8/12.
 */
public class Test {
    public static void main(String[] stra){
        long  ms = 300 * 1000 ;//毫秒数
        long hour = 3600*1000;
        String formatStr = "mm:ss";
        if(ms/hour >0){
            formatStr = "HH:mm:ss";
        }

        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);//初始化Formatter的转换格式。

        String hms = formatter.format(ms);
        System.out.println("hms:"+hms);
    }
}
