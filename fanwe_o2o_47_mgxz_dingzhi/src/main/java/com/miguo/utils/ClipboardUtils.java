package com.miguo.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import com.fanwe.utils.StringTool;

/**
 * 剪切板。
 * Created by zhouhy on 2016/11/29.
 */

public class ClipboardUtils {

    /**
     * 获取剪切板里面头条信息的值 。
     * @param activity
     */
    public static  String   checkCode(Activity activity) {
        ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        String code = "";
        if (clipboardManager.hasPrimaryClip()) {
            code = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
            if (!TextUtils.isEmpty(code) && code.length() >= 7) {
                if (!StringTool.checkStringPattern(code, StringTool.STRING_NUMBER)) {
                    code = "";
                }
            } else {
                code = "";
            }
        }
        return code;
    }
}
