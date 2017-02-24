package com.miguo.dao.impl;

import android.util.Log;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.miguo.dao.OnlinePayOrderContinuePaymentDao;
import com.miguo.entity.OnlinePayCancelBean;
import com.miguo.entity.OnlinePayOrderBean;
import com.miguo.view.BaseView;
import com.miguo.view.OnlinePayOrderContinuePaymentView;

import java.util.TreeMap;


/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/23.
 */

public class OnlinePayOrderContinuePaymentDaoImpl extends BaseDaoImpl implements OnlinePayOrderContinuePaymentDao{

    public OnlinePayOrderContinuePaymentDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void getOrderInfo(String orderId, String token) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("method", "OnlinePayOrderContinuePayment");
        map.put("order_id", orderId);
        map.put("token", token);
        OkHttpUtil.getInstance().get("", map, new MgCallback(OnlinePayOrderBean.class) {

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                OnlinePayOrderBean bean = (OnlinePayOrderBean) responseBody;
                if(isEmpty(bean)){
                    getListener().getOrderInfoError(BASE_ERROR_MESSAGE);
                    return;
                }
                if(bean.getStatusCode() == 200){
                    try{
                        getListener().getOrderInfoSuccess(bean.getResult().get(0).getBody().get(0));
                    }catch (Exception e){
                        getListener().getOrderInfoError(bean.getMessage());
                    }
                    return;
                }
                getListener().getOrderInfoError(bean.getMessage());
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                Log.d(tag, "onErrorResponse : " + message);
                getListener().getOrderInfoError(message);
            }

        });
    }

    @Override
    public OnlinePayOrderContinuePaymentView getListener() {
        return (OnlinePayOrderContinuePaymentView)baseView;
    }
}
