package com.miguo.presenters;

import com.miguo.entity.OnlinePayOrderPaymentBean;

/**
 * Created by Administrator on 2017/2/17.
 */

public interface OnlinePayOrderPaymentPresenter extends BasePresenter {

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

    OnlinePayOrderPaymentBean.Result.Body getBody();

}
