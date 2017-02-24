package com.miguo.dao;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/23.
 */

public interface OnlinePayOrderContinuePaymentDao extends BaseDao {
    /**
     * 在线买单继续支付接口，20170223需求变更新增。
     * @param orderId 订单id
     * @param token
     */
    void getOrderInfo(String orderId, String token);
}
