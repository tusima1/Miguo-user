package com.miguo.dao;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/23.
 */

public interface OnlinePayCancelDao extends BaseDao {

    /**
     * 	取消在线买单订单，取消订单会退还用户余额
     * @param order_id 订单id
     * @param token 用户token
     */
    void cancelOrder(String order_id, String token);

}
