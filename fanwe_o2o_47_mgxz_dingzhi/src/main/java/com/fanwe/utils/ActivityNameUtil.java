package com.fanwe.utils;


import java.util.HashMap;

/**
 * activity页面列表
 * Created by qiang.chen on 2016/12/27.
 */

public class ActivityNameUtil {
    private static ActivityNameUtil instance;
    private static HashMap mapActivity;

    private ActivityNameUtil() {
    }

    public static synchronized ActivityNameUtil getInstance() {
        if (instance == null) {
            instance = new ActivityNameUtil();
            generalActivityMap();
        }
        return instance;
    }

    public static String getPageName(String className) {
        String pageName = "";
        if (mapActivity == null) {
            return pageName;
        }
        try {
            int i = className.lastIndexOf(".");
            String tempStr = className.substring(i + 1);
            pageName = (String) mapActivity.get(tempStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageName;
    }

    private static void generalActivityMap() {
        mapActivity = new HashMap();
        mapActivity.put("InitAdvsMultiActivity", "App初始化页面");
        mapActivity.put("RepresentIncomeActivity", "代言收益");
        mapActivity.put("RepresentIntroduceActivity", "代言殿堂");
        mapActivity.put("LiveStartAuthActivity", "直播前提醒前往认证");
        mapActivity.put("LiveStartActivity", "直播前选择场所");
        mapActivity.put("LiveEndActivity", "直播结束页");
        mapActivity.put("MineShopActivity", "我的小店");
        mapActivity.put("LiveActivity", "直播页");
        mapActivity.put("LiveAuthActivity", "直播认证");
        mapActivity.put("LiveAuthTagActivity", "直播认证兴趣选择");

        mapActivity.put("GuideActivity", "引导页");
        mapActivity.put("PayHistoryActivity", "充值记录");
        mapActivity.put("AdviceActivity", "用户建议");
        mapActivity.put("SignActivity", "编辑个性签名");
        mapActivity.put("SexActivity", "编辑性别");
        mapActivity.put("AttentionListActivity", "关注列表");
        mapActivity.put("CollectListActivity", "收藏列表");
        mapActivity.put("UserHomeActivity", "网红主页");
        mapActivity.put("WXPayEntryActivity", "微信支付");
        mapActivity.put("HiHomeActivity", "首页");

        mapActivity.put("MapSearchActivity", "地图搜索");
        mapActivity.put("HiRegisterActivity", "用户注册");
        mapActivity.put("RegisterAgreementActivity", "注册协议");
        mapActivity.put("DistributionMyQRCodeActivity", "二维码名片");
        mapActivity.put("StoreListActivity", "店铺列表");
        mapActivity.put("CommentListActivity", "评论列表");
        mapActivity.put("ShopCartActivity", "购物车");
        mapActivity.put("ConfirmOrderActivity", "确认订单");
        mapActivity.put("CaptureResultWebActivity", "扫码后页面");
        mapActivity.put("PayActivity", "订单支付");

        mapActivity.put("MineTeamActivity", "我的战队");
        mapActivity.put("ModifyPasswordActivity", "密码");
        mapActivity.put("MyOrderListActivity", "订单列表");
        mapActivity.put("MyCouponListActivity", "团购券列表");
        mapActivity.put("ConfirmTopUpActivity", "会员升级");
        mapActivity.put("NoticeListActivity", "通知列表");
        mapActivity.put("CityListActivity", "城市列表");
        mapActivity.put("AddCommentActivity", "发布评论");
        mapActivity.put("HomeSearchActivity", "搜索页");
        mapActivity.put("NoticeDetailActivity", "通知详情");

        mapActivity.put("RouteDetailActivity", "路线规划");
        mapActivity.put("MyAccountActivity", "用户设置页面");
        mapActivity.put("UploadUserHeadActivity", "上传头像");
        mapActivity.put("DistributionWithdrawActivity", "提现");
        mapActivity.put("MyRedPaymentActivity", "红包选择");
        mapActivity.put("MyRedEnvelopeActivity", "红包列表");
        mapActivity.put("AccountMoneyActivity", "账户余额");
        mapActivity.put("WithdrawLogActivity", "提现日志");
        mapActivity.put("UserWithdrawLogActivity", "用户提现日志");
        mapActivity.put("BindMobileActivity", "绑定手机号");

        mapActivity.put("RefundApplicationActivity", "退款申请");
        mapActivity.put("WelcomeActivity", "欢迎页");
        mapActivity.put("TimeLimitActivity", "限时特惠");
        mapActivity.put("PlayBackActivity", "点播");
        mapActivity.put("HiShopDetailActivity", "商家详情");
        mapActivity.put("HiLoginActivity", "登录");
        mapActivity.put("HiWebPageActivity", "活动页");
        mapActivity.put("LotteryActivity", "抽奖");
        mapActivity.put("HiRepresentIntroduceActivity", "代言殿堂");
        mapActivity.put("HiUpdateUserWithEnoughMoneyActivity", "一键升级");

        mapActivity.put("HiUpdateUserActivity", "会员升级");
        mapActivity.put("HiWithdrawalConditionsActivity", "会员升级支付");
        mapActivity.put("GoodsDetailActivity", "商品详情");
        mapActivity.put("StoreLocationActivity", "商家地图");
        mapActivity.put("RouteInformationActivity", "查看线路");
        mapActivity.put("FansActivity", "我的粉丝");
        mapActivity.put("DistributionStoreWapActivity", "小店");
        mapActivity.put("SpecialTopicActivity", "专题");
        mapActivity.put("MyCouponDetailActivity", "团购券详情");
        mapActivity.put("WalletNewActivity", "我的钱包");

        mapActivity.put("BalanceActivity", "余额");
        mapActivity.put("RechargeDiamondActivity", "充值");
        mapActivity.put("MyIncomeActivity", "我的收益");
        mapActivity.put("RedPacketListActivity", "礼包");
        mapActivity.put("ExchangeDiamondActivity", "兑换果钻");
        mapActivity.put("ExchangeHistoryActivity", "果钻兑换记录");
        mapActivity.put("InviteActivity", "分享收益");
        mapActivity.put("RefundHistoryActivity", "退款记录");
        mapActivity.put("TuanListActivity", "团购列表");
    }
}
