package com.miguo.factory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fanwe.UserWithdrawLogActivity;
import com.fanwe.WithdrawLogActivity;
import com.fanwe.constant.ServerUrl;
import com.fanwe.mine.views.RepresentIncomeActivity;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.fanwe.user.view.InviteActivity;
import com.fanwe.user.view.MyCouponListActivity;
import com.fanwe.user.view.MyOrderListActivity;
import com.fanwe.user.view.RedPacketListActivity;
import com.miguo.app.HiShopDetailActivity;
import com.miguo.definition.ClassPath;
import com.miguo.definition.CommissionFromType;
import com.miguo.definition.IntentKey;
import com.miguo.definition.MessageType;
import com.miguo.entity.JpushMessageBean;
import com.miguo.entity.MessageListBean;
import com.miguo.utils.AppStateUtil;
import com.miguo.utils.BaseUtils;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/23.
 */

public class MessageTypeFactory {

    public static void jump(Context context, MessageListBean.Result.Body bean){
        jump(context, bean.getList_jump_target(), bean.getList_jump_paramater(), bean.getSystem_message_id());
    }

    public static void jump(Context context, JpushMessageBean bean){
        jump(context, bean.getPush_jump_target(), bean.getPush_jump_paramater(), bean.getSystem_message_id());
    }

    public static void jump(Context context, String type, String value, String systemMessageId){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        switch (type){
            /**
             * 不跳转
             */
            case MessageType.TARGET_NONE:
                break;
            /**
             * 系统消息
             */
            case MessageType.TARGET_SYSTEM_MESSAGE:
                intent.setClass(context, ClassNameFactory.getClass(ClassPath.MESSAGE_SYSTEM));
                intent.putExtra(IntentKey.SYSTEM_MESSAGE_URL, value);
                intent.putExtra(IntentKey.SYSTEM_MESSAGE_ID, systemMessageId);
                BaseUtils.jumpToNewActivity(context, intent);
                break;
            /**
             * 佣金消息列表
             */
            case MessageType.TARGET_MONEY_LIST:
                intent.setClass(context, ClassNameFactory.getClass(ClassPath.LIST_MESSAGE_COMMISSION));
                if(!AppStateUtil.isAppRunning(context)){
                    intent.putExtra(IntentKey.COMMISSION_NOTIFYCATION_FROM, CommissionFromType.NOTIFYCATION);
                }
                BaseUtils.jumpToNewActivity(context, intent);
                break;
            /**
             * 代言人佣金
             */
            case MessageType.TARGET_ENDOSER_COMMISION:
                intent.setClass(context, RepresentIncomeActivity.class);
                BaseUtils.jumpToNewActivity(context, intent);
                break;
            /**
             * 消费券列表
             */
            case MessageType.TARGET_COUPON_LIST:
                intent.setClass(context, MyCouponListActivity.class);
                BaseUtils.jumpToNewActivity(context, intent);
                break;
            /**
             * 代言殿堂
             */
            case MessageType.TARGET_ENDOSE_PALACE:
                intent.setClass(context, ClassNameFactory.getClass(ClassPath.REPRESENT_INTRODUCE_ACTIVITY));
                BaseUtils.jumpToNewActivity(context, intent);
                break;
            /**
             * 团购详情
             */
            case MessageType.TARGET_GROUPBY_DETAIL:
                intent.setClass(context, ClassNameFactory.getClass(ClassPath.GOODS_DETAIL_ACTIVITY));
                intent.putExtra(GoodsDetailActivity.EXTRA_GOODS_ID, value);
                BaseUtils.jumpToNewActivity(context, intent);
                break;
            /**
             * 订单列表
             */
            case MessageType.TARGET_ORDER_LIST:
                intent.setClass(context, MyOrderListActivity.class);
                intent.putExtra(MyOrderListActivity.EXTRA_ORDER_STATUS, value);
                BaseUtils.jumpToNewActivity(context, intent);
                break;
            /**
             * 我的奖品
             */
            case MessageType.TARGET_PRIZE_LIST:
                intent.setClass(context, ClassNameFactory.getClass(ClassPath.WEB_PAGE_ACTIVITY));
                Bundle bundle = new Bundle();
                bundle.putString(IntentKey.HOME_BANNER_WEB_PAGE, ServerUrl.getAppH5Url() + "activity/list");
                intent.putExtras(bundle);
                BaseUtils.jumpToNewActivity(context, intent);
                break;
            /**
             * 红包列表
             */
            case MessageType.TARGET_REDPACKET_LIST:
                intent.setClass(context, RedPacketListActivity.class);
                BaseUtils.jumpToNewActivity(context, intent);
                break;
            /**
             * 分享收益
             */
            case MessageType.TARGET_SHARE_PROFITS:
                intent.setClass(context, InviteActivity.class);
                BaseUtils.jumpToNewActivity(context, intent);
                break;
            /**
             * 门店详情
             */
            case MessageType.TARGET_SHOP_DETAIL:
                intent.setClass(context, ClassNameFactory.getClass(ClassPath.SHOP_DETAIL_ACTIVITY));
                intent.putExtra(HiShopDetailActivity.EXTRA_MERCHANT_ID, value);
                BaseUtils.jumpToNewActivity(context, intent);
                break;
            /**
             * 提现明细日志
             */
            case MessageType.TARGET_WITHDROW_DETAIL:
                intent.setClass(context, UserWithdrawLogActivity.class);
                intent.putExtra("money_type", 1);
                BaseUtils.jumpToNewActivity(context, intent);
                break;
        }
    }


}
