package com.fanwe.constant;

import android.content.Context;

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
        return true;
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//        Date curDate = new Date(System.currentTimeMillis());
//        String curStr = formatter.format(curDate);
//        Integer curInt = Integer.valueOf(curStr);
//        int lastInt = (int) SPUtil.getData(context, key, 0);
//        if (curInt - lastInt > 0){
//            //show
//            SPUtil.putData(context,key,curInt);
//            return true;
//        }else {
//            return false;
//        }
    }
}
