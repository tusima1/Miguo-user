package com.miguo.factory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.fanwe.seller.views.GoodsDetailActivity;
import com.miguo.app.HiShopDetailActivity;
import com.miguo.definition.AdspaceParams;
import com.miguo.definition.ClassPath;
import com.miguo.definition.IntentKey;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/11/2.
 */
public class AdspaceTypeFactory {

    public static void clickWidthType(String type, Activity context, String type_id){
        switch (type){
            case AdspaceParams.BANNER_TYPE_SALE_DETAIL:
                goGoodsDetail(context, type_id);
                break;
            case AdspaceParams.BANNER_TYPE_SHOP_DETAIL:
                goShopDetail(context, type_id);
                break;
            case AdspaceParams.BANNER_TYPE_TOPIC_DETAIL:
                goTopicDetail(context, type_id);
                break;
        }
    }

    /**
     * 跳转商品详情
     * @param context
     * @param type_id
     */
    private static void goGoodsDetail(Activity context, String type_id){
        Intent intent = new Intent(context, ClassNameFactory.getClass(ClassPath.GOODS_DETAIL_ACTIVITY));
        intent.putExtra(GoodsDetailActivity.EXTRA_GOODS_ID, type_id);
        com.miguo.live.views.utils.BaseUtils.jumpToNewActivity(context, intent);
    }

    /**
     * 跳转商家详情
     * @param context
     * @param type_id
     */
    private static void goShopDetail(Activity context, String type_id){
        Intent intent = new Intent(context, ClassNameFactory.getClass(ClassPath.SHOP_DETAIL_ACTIVITY));
        intent.putExtra(HiShopDetailActivity.EXTRA_MERCHANT_ID, type_id);
        com.miguo.live.views.utils.BaseUtils.jumpToNewActivity(context, intent);
    }

    /**
     * 主题详情
     * @param context
     * @param type_id
     */
    private static void goTopicDetail(Activity context, String type_id){
        Intent intent = new Intent(context, ClassNameFactory.getClass(ClassPath.SPECIAL_TOPIC_ACTIVITY));
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.SPECIAL_TOPIC_ID, type_id);
        intent.putExtras(bundle);
        com.miguo.live.views.utils.BaseUtils.jumpToNewActivity(context, intent);
    }



}
