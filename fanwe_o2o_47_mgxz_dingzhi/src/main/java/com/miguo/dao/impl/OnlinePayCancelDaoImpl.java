package com.miguo.dao.impl;

import android.util.Log;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.miguo.dao.OnlinePayCancelDao;
import com.miguo.entity.OnlinePayCancelBean;
import com.miguo.view.BaseView;
import com.miguo.view.OnlinePayCancelView;

import java.util.TreeMap;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/23.
 */

public class OnlinePayCancelDaoImpl extends BaseDaoImpl implements OnlinePayCancelDao {

    public OnlinePayCancelDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void cancelOrder(String order_id, String token) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("method", "OnlinePayCancel");
        map.put("order_id", order_id);
        map.put("token", token);
        OkHttpUtil.getInstance().get("", map, new MgCallback(OnlinePayCancelBean.class) {

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                OnlinePayCancelBean bean = (OnlinePayCancelBean) responseBody;
                if(isEmpty(bean)){
                    getListener().cancelError(BASE_ERROR_MESSAGE);
                    return;
                }
                if(bean.getStatusCode() == 200){
                    getListener().cancelSuccess();
                    return;
                }
                getListener().cancelError(bean.getMessage());
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                Log.d(tag, "onErrorResponse : " + message);
                getListener().cancelError(message);
            }

        });
    }

    @Override
    public OnlinePayCancelView getListener() {
        return (OnlinePayCancelView)baseView;
    }
}
