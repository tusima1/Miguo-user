package com.fanwe.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/18.
 */
public class StringTool {

    public static final String STRING_NUMBER="number";
    public static final String STRING_ENGLISH="English";
    public static final String STRING_CHINESE="Chinese";
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

    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        if (str == null || str.trim().equals("") || str.trim().equalsIgnoreCase("null")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     *
     * @param s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int lengthEnglish(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }


    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5
     *
     * @param s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static double getLengthChinese(String s) {
        double valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < s.length(); i++) {
            // 获取一个字符
            String temp = s.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为1
                valueLength += 1;
            } else {
                // 其他字符长度为0.5
                valueLength += 0.5;
            }
        }
        //进位取整
        return Math.ceil(valueLength);
    }

    /**
     * 查找表情
     *
     * @param source
     * @return
     */
    public static boolean findEmoji(String source) {
        if (source != null) {
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 过滤表情
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        if (source != null) {
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                source = emojiMatcher.replaceAll("*");
                return source;
            }
            return source;
        }
        return source;
    }

    /**
     * 距离转换
     */
    public static String getDistance(String value) {
        float distance = DataFormat.toFloat(value);
        if (distance <= 1000) {
            return distance + "m";
        } else {
            float y = distance / 1000;
            return DataFormat.toDoubleTwo(y + "") + "km";
        }
    }

    /**
     * 判断输入的字符类型。
     *
     * @param data 需要判断的字符
     * @param type number / English/Chinese
     * @return
     */
    public static boolean checkStringPattern(String data, String type) {
        boolean result = false;
        Pattern p = null;
        if (TextUtils.isEmpty(data)) {
            return result;
        }
        switch (type) {
            case STRING_NUMBER:
                p = Pattern.compile("[0-9]*");
                break;
            case STRING_ENGLISH:
                p = Pattern.compile("[a-zA-Z]");
                break;
            case STRING_CHINESE:
                p = Pattern.compile("[\u4e00-\u9fa5]");
                break;
            default:
                break;
        }
        Matcher m = p.matcher(data);

        if (m.matches()) {
            result = true;
        }
        return result;
    }
}
