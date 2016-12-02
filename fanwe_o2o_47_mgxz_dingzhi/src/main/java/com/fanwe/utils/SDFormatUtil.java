package com.fanwe.utils;

import android.text.TextUtils;

import com.fanwe.library.utils.SDTypeParseUtil;

import java.text.NumberFormat;

public class SDFormatUtil {

    public static String formatMoneyChina(String money) {
        if (!TextUtils.isEmpty(money)) {
            String moneyRound = String.valueOf(SDNumberUtil.round(SDTypeParseUtil.getDouble(money), 2));
            money = moneyRound;
            if (money.contains(".")) {
                int decimalIndex = money.indexOf(".");
                String decimalPart = money.substring(decimalIndex + 1);
                if ("0".equals(decimalPart)) {
                    money = money.substring(0, decimalIndex);
                }
            }
            //精确两位
            return "￥" + DataFormat.toDoubleTwo(money);
        } else {
            return "￥0";
        }
    }

    public static String formatMoneyChina(double money) {
        return formatMoneyChina(String.valueOf(money));
    }

    public static float stringToFloat(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0.00f;
        } else {
            return Float.valueOf(value);
        }
    }

    /**
     * 直接数据过万改成 2w这样的形式
     * @param count
     * @return
     */
    public static String formatTenThouthand(String count) {
        if (!TextUtils.isEmpty(count)) {
           float value = stringToFloat(count);
            if(value>=10000){
             return  DataFormat.toDoubleTwo(value/10000)+"W";
            }else{
                return count;
            }
        } else {
            return "0";
        }
    }
    public static int stringToInteger(String number) {
        if (TextUtils.isEmpty(number)) {
            return 0;
        }
        return Integer.valueOf(number);
    }

    public static String formatNumberString(String formatString, int number) {
        if (formatString != null && formatString.length() > 0) {
            NumberFormat format = NumberFormat.getNumberInstance();
            format.setMaximumFractionDigits(number);
            try {
                return format.format(Double.valueOf(formatString));
            } catch (Exception e) {
                return null;
            }

        } else {
            return formatString;
        }
    }

    /**
     * 判断第一个值 是否大于等于第二个值.
     * @param value1  第一个值
     * @param value2  第二个值
     * @return boolean
     */
    public static boolean compareNumber(String value1,String value2){
        if(TextUtils.isEmpty(value1.trim())){
            value1 = "0";
        }
        if(TextUtils.isEmpty(value2.trim())){
            value2 = "0";
        }
        float f1 = Float.valueOf(value1.trim());
        float f2 = Float.valueOf(value2.trim());
        if(f1>=f2){
            return true;
        }else{
            return false;
        }
    }
    public static String formatNumberDouble(double formatDouble, int number) {
        return formatNumberString(String.valueOf(formatDouble), number);
    }

    public static String formatDuring(long mss) {
        long days = getDuringDay(mss);
        long hours = getDuringHours(mss);
        long minutes = getDuringMinutes(mss);
        long seconds = getDuringSeconds(mss);
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days + "天");
        }
        if (hours > 0) {
            sb.append(hours + "小时");
        }
        if (minutes > 0) {
            sb.append(minutes + "分钟");
        }
        return sb.toString();
    }

    public static long getDuringDay(long mss) {
        return mss / getDaysMilliseconds();
    }

    public static long getDuringHours(long mss) {
        return (mss % (getDaysMilliseconds())) / (getHoursMilliseconds());
    }

    public static long getDuringMinutes(long mss) {
        return (mss % (getHoursMilliseconds())) / (getMinutesMilliseconds());
    }

    public static long getDuringSeconds(long mss) {
        return (mss % (getMinutesMilliseconds())) / getSecondsMilliseconds();
    }

    public static long getDaysMilliseconds() {
        return (1000 * 60 * 60 * 24);
    }

    public static long getHoursMilliseconds() {
        return (1000 * 60 * 60);
    }

    public static long getMinutesMilliseconds() {
        return (1000 * 60);
    }

    public static long getSecondsMilliseconds() {
        return (1000);
    }
}
