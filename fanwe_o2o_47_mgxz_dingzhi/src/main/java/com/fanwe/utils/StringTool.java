package com.fanwe.utils;

import android.text.TextUtils;

/**
 * Created by Administrator on 2016/8/18.
 */
public class StringTool {
    /**
     * 字符串处理
     *
     * @param input
     * @param length
     * @param tail
     * @return
     */
    public static String getStringFixed(String input, int length, String tail) {
        if (length < 1) {
            return "";
        } else {
            if (length > input.length()) {
                return input;
            } else {
                if (!TextUtils.isEmpty(tail)) {
                    return input.substring(0, length) + tail;
                } else {
                    return input.substring(0, length) + "...";
                }
            }
        }
    }
}
