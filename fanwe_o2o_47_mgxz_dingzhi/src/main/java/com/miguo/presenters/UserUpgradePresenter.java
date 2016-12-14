package com.miguo.presenters;

/**
 * Created by zlh on 2016/12/14.
 * 获取用户升级信息
 * 用户升级（全额支付、预扣费支付、微信支付、支付宝支付）
 */
public interface UserUpgradePresenter extends BasePresenter {

    /**
     * 获取用户升级信息
     */
    void getUserUpgradeInfo();

    /**
     * 全余额支付升级
     */
    void userUpgradeByAccount();

    /**
     * 预扣费升级
     */
    void userUpgradeByWithholding();

    /**
     * 微信支付升级
     */
    void userUpgradeByWechat();

    /**
     * 支付宝支付升级
     */
    void userUpgradeByAlipay();

}
