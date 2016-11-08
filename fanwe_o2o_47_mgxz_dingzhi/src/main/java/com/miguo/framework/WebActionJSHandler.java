package com.miguo.framework;

import android.webkit.JavascriptInterface;

import com.miguo.app.HiBaseActivity;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/8.
 */
public class WebActionJSHandler {

    HiBaseActivity baseActivity;

    public WebActionJSHandler(HiBaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    /**
     * 设置标题
     * @param title
     */
    @JavascriptInterface
    public void setTitle(String title){
        baseActivity.showToastWithShortTime(title);
    }

    @JavascriptInterface
    public void addCart(String cart_type,String goods_id,String add_goods_num,String fx_user_id,String roomId){
        baseActivity.showToastWithShortTime("add cart cart type: " + cart_type + " ,goods id: " + goods_id);
    }

    @JavascriptInterface
    public void goTuanInfo(String tuan_id){
        baseActivity.showToastWithShortTime("go tuan info: " + tuan_id);
    }

    @JavascriptInterface
    public void goShopInfo(String shop_id){
        baseActivity.showToastWithShortTime("go shop info: " + shop_id);
    }


}
