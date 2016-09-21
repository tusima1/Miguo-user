package com.fanwe.test;

import android.content.Intent;

import com.fanwe.StoreDetailActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/12.
 */


public class TestUrlCompile {
    /**
     *
     *   门店详情  ^https?://[^/]+.mgxz.com/index/retail/id/([^/\s]+)
     团购详情  ^https?://[^/]+.mgxz.com/index/detail/id/([^/\s]+)
     他的小店  ^https?://[^/]+.mgxz.com/user/shop/uid/([^/\s]+)

     * @param args
     */
    public static void main(String args[]) {

        String value1 = "http://m.w2.mgxz.com/user/shop/uid/88025143-194f-4705-991b-7f5a3587dc9c";
        String str1="^https?://[^/]+.mgxz.com/index/retail/id/([^/\\s]+)";
        String str2="^https?://[^/]+.mgxz.com/user/shop/uid/([^/\\s]+)";




        TestUrlCompile cc = new TestUrlCompile();
        cc.getCompleteUrl(value1,str2);

    }

    /**
     * //获取完整的域名
     *
     * @param text 获取浏览器分享出来的text文本
     */
    public static String getCompleteUrl(String text,String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(text);

        boolean result = matcher.find();
        System.out.println("result:"+result);
        String extra_merchant_id =  text.split("\\/")[text.split("\\/").length -1];
        System.out.println("extra_merchant_id:"+extra_merchant_id);
         String key = matcher.group();
        System.out.println("key:"+key);
        return matcher.group();

    }
}