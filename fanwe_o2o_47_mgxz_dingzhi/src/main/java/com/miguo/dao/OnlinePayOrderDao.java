package com.miguo.dao;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/16.
 */

public interface OnlinePayOrderDao extends BaseDao {

    /**
     * 预留红包功能的接口
     * @param total_amount 总金额
     * @param is_continue 继续支付（优惠已过二次点击支付）
     * @param red_packet_id 红包id，未使用该功能
     * @param token 用户token
     * @param shop_id 门店id
     * @param exclude_amount 不参与优惠金额
     */
    void onlinePayOrder(String total_amount, String exclude_amount, int is_continue, String red_packet_id, String token, String shop_id);
    void onlinePayOrder(String total_amount, String exclude_amount, String red_packet_id, String token, String shop_id);

    /**
     * 首次点击确认订单调用接口
     * @param total_amount 总金额
     * @param exclude_amount 不参与优惠金额
     * @param token 用户token
     * @param shop_id 门店id
     */
    void onlinePayOrder(String total_amount, String exclude_amount, String token, String shop_id);
    void onlinePayOrder(double total_amount, double exclude_amount, String token, String shop_id);

    /**
     * 第一次点击优惠失效后，点击继续支付调用接口
     * @param total_amount 总金额
     * @param is_continue 继续支付填1
     * @param exclude_amount 不参与优惠金额
     * @param token 用户token
     * @param shop_id 门店id
     */
    void onlinePayOrder(String total_amount, String exclude_amount,int is_continue, String token, String shop_id);
    void onlinePayOrder(double total_amount, double exclude_amount,int is_continue, String token, String shop_id);


}
