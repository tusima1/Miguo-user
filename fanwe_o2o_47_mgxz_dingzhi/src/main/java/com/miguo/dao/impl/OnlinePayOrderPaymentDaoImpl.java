package com.miguo.dao.impl;

import com.fanwe.app.App;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.miguo.dao.OnlinePayOrderPaymentDao;
import com.miguo.definition.PaymentId;
import com.miguo.entity.OnlinePayOrderPaymentBean;
import com.miguo.view.BaseView;
import com.miguo.view.OnlinePayOrderPaymentView;

import java.util.TreeMap;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/17.
 */

public class OnlinePayOrderPaymentDaoImpl extends BaseDaoImpl implements OnlinePayOrderPaymentDao {


    public OnlinePayOrderPaymentDaoImpl(BaseView baseView) {
        super(baseView);
    }

    public void pay(String orderId, int userAmount,final String payment){
        TreeMap<String , String> params = new TreeMap<>();
        params.put("token", App.getInstance().getToken());
        params.put("payment", payment);
        params.put("order_id", orderId);
        params.put("use_balance", userAmount + "");
        params.put("method", "OnlinePayOrderPayment");
        OkHttpUtil.getInstance().post(null, params, new MgCallback(OnlinePayOrderPaymentBean.class) {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().payError(message);
            }

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                OnlinePayOrderPaymentBean bean = (OnlinePayOrderPaymentBean)responseBody;
                if(null == bean){
                    getListener().payError(BASE_ERROR_MESSAGE);
                    return;
                }

                if(bean.getStatusCode() != 200){
                    getListener().payError(bean.getMessage());
                    return;
                }

                if(isEmpty(bean.getResult())  || isEmpty(bean.getResult().get(0)) || isEmpty(bean.getResult().get(0)) ||isEmpty(bean.getResult().get(0).getBody()) || isEmpty( bean.getResult().get(0).getBody().get(0))){
                    getListener().payError(BASE_ERROR_MESSAGE);
                    return;
                }

                /**
                 * 支付成功/获取支付信息成功
                 */
                handleUpgradeSuccess(payment, bean.getResult().get(0).getBody().get(0));
            }
        });
    }

    /**
     * 根据payment_id判断回调成功
     * @param payment_id 支付方式
     * @param config 如果是第三方支付，需要config信息
     */
    private void handleUpgradeSuccess(String payment_id, OnlinePayOrderPaymentBean.Result.Body config){
        switch (payment_id){
            case PaymentId.ACCOUNT:
                if(config.getOrder_info().getOrder_status() >= 3){
                    getListener().amountPaySuccess(config);
                    return;
                }
                getListener().payError("支付失败！");
                break;
            case PaymentId.WECHAT:
                getListener().wechatPaySuccess(config);
                break;
            case PaymentId.ALIPAY:
                getListener().alipaySuccess(config);
                break;
        }
    }

    @Override
    public void alipay(String orderId, int userAmount) {
        pay(orderId,userAmount, PaymentId.ALIPAY);
    }

    @Override
    public void wechat(String orderId, int userAmount) {
        pay(orderId,userAmount, PaymentId.WECHAT);
    }

    @Override
    public void amount(String orderId) {
        pay(orderId, 1, PaymentId.ACCOUNT);
    }

    @Override
    public OnlinePayOrderPaymentView getListener() {
        return (OnlinePayOrderPaymentView)baseView;
    }



}
