package com.miguo.live.model.getWallet;

/**
 * Created by didik on 2016/9/19.
 */
public class ModelMyWallet {
//    "total_user_commission":"1099347.81",//总佣金
//            "miguobean":"50.00",//米果豆
//            "miguobean_money":"70.00",//米果豆兑换的金额
//            "now_user_account_money":"100000.00",//当前余额
//            "share_money":"90.00",//分享金额
//            "common_diamond":"30.00"//分享钻
    private String total_user_commission;
    private String miguobean;
    private String miguobean_money;
    private String now_user_account_money;
    private String share_money;
    private String common_diamond;

    public String getTotal_user_commission() {
        return total_user_commission;
    }

    public void setTotal_user_commission(String total_user_commission) {
        this.total_user_commission = total_user_commission;
    }

    public String getMiguobean() {
        return miguobean;
    }

    public void setMiguobean(String miguobean) {
        this.miguobean = miguobean;
    }

    public String getMiguobean_money() {
        return miguobean_money;
    }

    public void setMiguobean_money(String miguobean_money) {
        this.miguobean_money = miguobean_money;
    }

    public String getNow_user_account_money() {
        return now_user_account_money;
    }

    public void setNow_user_account_money(String now_user_account_money) {
        this.now_user_account_money = now_user_account_money;
    }

    public String getShare_money() {
        return share_money;
    }

    public void setShare_money(String share_money) {
        this.share_money = share_money;
    }

    public String getCommon_diamond() {
        return common_diamond;
    }

    public void setCommon_diamond(String common_diamond) {
        this.common_diamond = common_diamond;
    }
}
