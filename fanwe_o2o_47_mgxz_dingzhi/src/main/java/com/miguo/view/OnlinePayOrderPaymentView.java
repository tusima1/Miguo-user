package com.miguo.view;

import com.miguo.entity.OnlinePayOrderPaymentBean;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/17.
 */

public interface OnlinePayOrderPaymentView extends BaseView {

    /**
     * 余额支付成功
     */
    void amountPaySuccess(OnlinePayOrderPaymentBean.Result.Body body);

    /**
     * 获取微信支付信息成功
     * @param config 支付信息
     */
    void wechatPaySuccess(OnlinePayOrderPaymentBean.Result.Body config);

    /**
     * 获取支付宝支付信息成功
     * @param config
     */
    void alipaySuccess(OnlinePayOrderPaymentBean.Result.Body config);

    /**
     * 支付失败
     * @param message
     */
    void payError(String message);

}
