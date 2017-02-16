package com.miguo.dao.impl;
import com.alibaba.fastjson.JSON;
import com.fanwe.app.App;
import com.fanwe.base.Root;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.user.UserConstants;
import com.google.gson.Gson;
import com.miguo.dao.OnlinePayOrderDao;
import com.miguo.entity.OnlinePayOrderBean;
import com.miguo.view.BaseView;
import com.miguo.view.OnlinePayOrderView;

import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/16.
 */

public class OnlinePayOrderDaoImpl extends BaseDaoImpl implements OnlinePayOrderDao {


    public OnlinePayOrderDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void onlinePayOrder(String total_amount, String exclude_amount, int is_continue, String red_packet_id, String token, String shop_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("total_amount", total_amount);
        params.put("exclude_amount", exclude_amount);
        if(is_continue != -1){
            params.put("is_continue", "1");
        }
        if(!isEmpty(red_packet_id)){
            params.put("red_packet_id", red_packet_id);
        }
        params.put("token", App.getInstance().getToken());
        params.put("shop_id", shop_id);
        params.put("method", UserConstants.ONLINE_PAY_ORDER);

        OkHttpUtil.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                OnlinePayOrderBean bean = new Gson().fromJson(responseBody, OnlinePayOrderBean.class);
                if(isEmpty(bean)){
                    getListener().onlinePayOrderError(BASE_ERROR_MESSAGE);
                    return;
                }
                if(bean.getStatusCode() != 200 && bean.getStatusCode()!=370){
                    getListener().onlinePayOrderError(bean.getMessage());
                    return;
                }
                /**
                 * 买单优惠已失效
                 */
                if(bean.getStatusCode() == 370){
                    getListener().offerHasExpired();
                    return;
                }
                if(bean.getStatusCode() == 200){
                    try{
                        getListener().onlinePayOrderSuccess(bean.getResult().get(0).getBody().get(0));
                        return;
                    }catch (Exception e){
                        getListener().onlinePayOrderError(BASE_ERROR_MESSAGE);
                        return;
                    }
                }

                getListener().onlinePayOrderError(bean.getMessage());

            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().onlinePayOrderError(BASE_ERROR_MESSAGE);
            }
        });
    }

    /**
     * 继续支付
     * @param total_amount
     * @param exclude_amount
     * @param is_continue
     * @param token
     * @param shop_id
     */
    @Override
    public void onlinePayOrder(double total_amount, double exclude_amount, int is_continue, String token, String shop_id) {
        onlinePayOrder(total_amount + "", exclude_amount + "", is_continue, token, shop_id);
    }

    /**
     * 继续支付
     * @param total_amount 总金额
     * @param exclude_amount 不参与优惠金额
     * @param is_continue 继续支付填1
     * @param token 用户token
     * @param shop_id 门店id
     */
    @Override
    public void onlinePayOrder(String total_amount, String exclude_amount, int is_continue, String token, String shop_id) {
        onlinePayOrder(total_amount, exclude_amount, is_continue, null, token, shop_id);
    }

    /**
     * 带红包支付
     * @param total_amount
     * @param exclude_amount
     * @param red_packet_id
     * @param token
     * @param shop_id
     */
    @Override
    public void onlinePayOrder(String total_amount, String exclude_amount, String red_packet_id, String token, String shop_id) {
        onlinePayOrder(total_amount, exclude_amount, -1, red_packet_id, token, shop_id);
    }

    /**
     * 确认订单，无红包
     * @param total_amount 总金额
     * @param exclude_amount 不参与优惠金额
     * @param token 用户token
     * @param shop_id 门店id
     */
    @Override
    public void onlinePayOrder(String total_amount, String exclude_amount, String token, String shop_id) {
        onlinePayOrder(total_amount, exclude_amount, -1, null, token, shop_id);
    }

    /**
     * 确认订单，无红包
     * @param total_amount
     * @param exclude_amount
     * @param token
     * @param shop_id
     */
    @Override
    public void onlinePayOrder(double total_amount, double exclude_amount, String token, String shop_id) {
        onlinePayOrder(total_amount + "", exclude_amount + "", token , shop_id);
    }

    @Override
    public OnlinePayOrderView getListener() {
        return (OnlinePayOrderView)baseView;
    }
}
