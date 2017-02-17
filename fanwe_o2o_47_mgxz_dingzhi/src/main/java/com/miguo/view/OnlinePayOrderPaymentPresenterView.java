package com.miguo.view;

import com.miguo.entity.OnlinePayOrderPaymentBean;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/17.
 */
public interface OnlinePayOrderPaymentPresenterView extends BaseView{

    /**
     * 支付成功（不管哪种方式支付成功）
     * @param body
     */
    void paySuccess(OnlinePayOrderPaymentBean.Result.Body body);

    /**
     * 支付失败
     * @param message
     */
    void payError(String message);

}
