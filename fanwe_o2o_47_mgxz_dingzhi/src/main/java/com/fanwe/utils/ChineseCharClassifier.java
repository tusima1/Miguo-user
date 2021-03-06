package com.fanwe.utils;

import android.os.Build;

import java.util.regex.Pattern;

/**
 * 中文字符和中文符号判断。
 * Created by zhouhy on 2016/10/31.
 */
public class ChineseCharClassifier {

    /**
     * 使用UnicodeBlock方法判断
     *
     * @param c
     * @return
     */
    public static boolean isChineseByBlock(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT);
    }


    /**
     * 根据UnicodeBlock方法判断中文标点符号
     *
     * @param c
     * @return
     */
    public static boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            Class<?> clz = Character.UnicodeBlock.class;
            try {
                clz.getDeclaredField("VERTICAL_FORMS");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                return false;
            }
            return (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                    || ub == Character.UnicodeBlock.VERTICAL_FORMS);
        } else {
            return (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
            );
        }

    }

    /**
     * 使用Unicode编码范围来判断汉字；这个方法不准确,因为还有很多汉字不在这个范围之内
     */
    public static boolean isChineseByRange(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FCC]+");
        return pattern.matcher(str.trim()).find();
    }
}
