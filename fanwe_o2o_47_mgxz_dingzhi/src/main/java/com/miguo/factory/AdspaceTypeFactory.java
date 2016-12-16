package com.miguo.factory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fanwe.seller.views.GoodsDetailActivity;
import com.miguo.app.HiShopDetailActivity;
import com.miguo.definition.AdspaceParams;
import com.miguo.definition.ClassPath;
import com.miguo.definition.IntentKey;
import com.miguo.definition.RequestCode;
import com.miguo.live.views.utils.BaseUtils;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/11/2.
 */
public class AdspaceTypeFactory {

    public static void clickWidthType(String type, AppCompatActivity context, String type_id){
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
            case AdspaceParams.BANNER_TYPE_URL:
                goWebAction(context, type_id);
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

    /**
     * 活动页详情
     * @param context
     * @param type_id
     */
    private static void goWebAction(Activity context, String type_id){
        Intent intent = new Intent(context, ClassNameFactory.getClass(ClassPath.WEB_PAGE_ACTIVITY));
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.HOME_BANNER_WEB_PAGE, type_id);
        intent.putExtras(bundle);
        BaseUtils.jumpToNewActivityForResult(context, intent, RequestCode.HOME_WEB_PAGE);
    }



}
