package com.miguo.dao;

/**
 * Created by zlh on 2016/12/14.
 */
public interface UserUpgradeOrderDao extends BaseDao {

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
