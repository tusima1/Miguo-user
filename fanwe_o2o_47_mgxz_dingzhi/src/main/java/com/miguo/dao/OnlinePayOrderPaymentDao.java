package com.miguo.dao;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/17.
 */

public interface OnlinePayOrderPaymentDao extends BaseDao {

    /**
     * 微信支付
     * @param orderId
     * @param useAmount
     */
    void wechat(String orderId, int useAmount);

    /**
     * 支付宝支付
     * @param orderId
     * @param useAmount
     */
    void alipay(String orderId, int useAmount);

    /**
     * 余额支付
     *
     */
    void amount(String orderId);
}
