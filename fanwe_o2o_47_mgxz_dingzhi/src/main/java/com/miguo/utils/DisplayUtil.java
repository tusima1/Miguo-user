package com.miguo.utils;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by didik on 2016/7/30.
 * 关于dp px sp 转换
 */
public class DisplayUtil {
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 返回指定长宽的url地址，目前针对七牛做了处理、
     * @param url  原地址
     * @param width 宽度
     * @param height 高度
     * @return
     */
    public static String qiniuUrlExchange(String url,int width,int height){
        String result = url;
        if(width<50){
            width = 50;
        }

        if(height <50){
            height = 50;
        }
        width = width*2;
        height = height*2;
        if(!TextUtils.isEmpty(url)&&url.startsWith("http://")){
            url =url+"?imageView2/5/w/"+width+"/h/"+height+"/format/jpg/q/100";

            result = url;
        }
        return result;
    }
}
