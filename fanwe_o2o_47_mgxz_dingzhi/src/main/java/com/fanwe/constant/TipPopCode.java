package com.fanwe.constant;

import android.content.Context;
import android.icu.text.SimpleDateFormat;

import com.miguo.utils.dev.SPUtil;

import java.util.Date;

/**
 * Created by didik 
 * Created time 2016/12/12
 * Description: 
 */

public class TipPopCode {
    public static final String Goods = "goods";//商品详情
    public static final String Topic = "topic";//专题页面
    public static final String Shops = "shops";//门店详情

    public static boolean checkDate(Context context, String key) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());
        String curStr = formatter.format(curDate);
        Integer curInt = Integer.valueOf(curStr);
        int lastInt = (int) SPUtil.getData(context, key, 0);
        if (curInt - lastInt > 0){
            //show
            SPUtil.putData(context,key,curInt);
            return true;
        }else {
            return false;
        }
    }
}
