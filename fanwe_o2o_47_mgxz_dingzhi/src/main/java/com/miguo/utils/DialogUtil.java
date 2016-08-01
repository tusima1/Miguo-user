package com.miguo.utils;

import android.app.Activity;
import android.app.Dialog;

import com.fanwe.o2o.miguo.R;

/**
 * Created by didik on 2016/8/1.
 * dialog工具类(live)
 */
public class DialogUtil {

    private Activity mActivity;

    public DialogUtil(Activity activity) {
        this.mActivity=activity;
    }

    public void createDialog(){
        Dialog dialog=new Dialog(mActivity, R.style.dialog);
        /*<style name="dialog" parent="@android:style/Theme.Dialog">
        <item name="android:windowFrame">@null</item>
        <item name="android:windowIsFloating">false</item>
        <item name="android:windowIsTranslucent">false</item>
        <item name="android:windowNoTitle">true</item>
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:windowCloseOnTouchOutside">true</item>
        <item name="android:backgroundDimEnabled">true</item>
        </style>*/


    }
}
