package com.miguo.framework;

import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.fanwe.seller.views.GoodsDetailActivity;
import com.miguo.app.HiBaseActivity;
import com.miguo.app.HiShopDetailActivity;
import com.miguo.app.HiWebPageActivity;
import com.miguo.category.HiWebPageCategory;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;
import com.miguo.live.views.utils.BaseUtils;

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
        if(getCategory() != null){
            getCategory().updateTitle(title);
        }
    }

    @JavascriptInterface
    public void addCart(String cart_type,String goods_id,String add_goods_num,String fx_user_id,String roomId){
    }

    @JavascriptInterface
    public void goTuanInfo(String tuan_id){
        Intent intent=new Intent(baseActivity , ClassNameFactory.getClass(ClassPath.GOODS_DETAIL_ACTIVITY));
        intent.putExtra(GoodsDetailActivity.EXTRA_GOODS_ID,tuan_id);
        BaseUtils.jumpToNewActivityWithFinish(baseActivity, intent);
    }

    @JavascriptInterface
    public void goShopInfo(String shop_id){
        Intent intent=new Intent(baseActivity , ClassNameFactory.getClass(ClassPath.SHOP_DETAIL_ACTIVITY));
        intent.putExtra(HiShopDetailActivity.EXTRA_MERCHANT_ID,shop_id);
        BaseUtils.jumpToNewActivityWithFinish(baseActivity, intent);
    }

    public HiWebPageCategory getCategory(){
        return baseActivity instanceof HiWebPageActivity ? (HiWebPageCategory)baseActivity.getCategory() : null;
    }

    public void goLive(String tag_id){

    }




}
