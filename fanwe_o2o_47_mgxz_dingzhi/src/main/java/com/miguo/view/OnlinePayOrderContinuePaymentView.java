package com.miguo.view;

import com.miguo.entity.OnlinePayOrderBean;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/23.
 */

public interface OnlinePayOrderContinuePaymentView extends BaseView {

    void getOrderInfoSuccess(OnlinePayOrderBean.Result.Body orderInfo);
    void getOrderInfoError(String message);
}
