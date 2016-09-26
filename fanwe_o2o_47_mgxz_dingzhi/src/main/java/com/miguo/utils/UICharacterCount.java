package com.miguo.utils;

/**
 * 获取字符串长度(类似 TextView EMS 属性)
 * Created by didik on 2016/9/26.
 */
public class UICharacterCount {
    /***
     * 判断字符是否为中文
     * @param ch 需要判断的字符
     * @return 中文返回true，非中文返回false
     */
    private static boolean isChinese(char ch) {
        //获取此字符的UniCodeBlock
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
        //  GENERAL_PUNCTUATION 判断中文的“号
        //  CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
        //  HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * 返回字符串的长度 规则如下: 以一个中文字符为单位,数字,字母,英文字符为0.5个单位.
     * @param str
     * @return float数值
     */
    public static float getCount(String str) {
        if (null == str || str.equals("")) {
            return 0;
        }
        int enCharacter=0;
        int numberCharacter=0;
        int spaceCharacter=0;
        int chCharacter=0;
        int otherCharacter=0;
        for (int i = 0; i < str.length(); i++) {
            char tmp = str.charAt(i);
            if ((tmp >= 'A' && tmp <= 'Z') || (tmp >= 'a' && tmp <= 'z')) {
                enCharacter ++;
            } else if ((tmp >= '0') && (tmp <= '9')) {
                numberCharacter ++;
            } else if (tmp ==' ') {
                spaceCharacter ++;
            } else if (isChinese(tmp)) {
                chCharacter ++;
            } else {
                otherCharacter ++;
            }
        }
        int totalCount=enCharacter + numberCharacter + spaceCharacter + chCharacter*2 +otherCharacter;
        return totalCount*1.0f/2;
    }
}
