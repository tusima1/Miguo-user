package com.miguo.live.views.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 狗蛋哥 on 16/3/7.
 */
public class ToasUtil {


    public static void showToastWithShortTime(Context context, String msg){
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showToastWithLongTime(Context context, String msg){
        showToast(context, msg, Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, String msg, int time){
        Toast.makeText(context, msg , time).show();
    }

}
