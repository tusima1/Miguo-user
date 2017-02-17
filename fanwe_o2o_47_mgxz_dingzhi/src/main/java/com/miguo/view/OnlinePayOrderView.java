package com.miguo.view;

import com.miguo.entity.OnlinePayOrderBean;

/**
 * Created by Administrator on 2017/2/16.
 */

public interface OnlinePayOrderView extends BaseView{

    /**
     * 确认订单成功
     * @param orderInfo 订单信息
     */
    void onlinePayOrderSuccess(OnlinePayOrderBean.Result.Body orderInfo);

    /**
     * 优惠已失效，需要问用户是否继续支付
     */
    void offerHasExpired();

    /**
     * 获取订单错误
     * @param message
     */
    void onlinePayOrderError(String message);

}
