package com.miguo.dao.impl;

import android.util.Log;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.miguo.dao.BeforeOnlinePayDao;
import com.miguo.entity.BeforeOnlinePayBean;
import com.miguo.entity.HiFunnyTabBean;
import com.miguo.view.BaseView;
import com.miguo.view.BeforeOnlinePayView;

import java.util.TreeMap;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/15.
 */

public class BeforeOnlinePayDaoImpl extends BaseDaoImpl implements BeforeOnlinePayDao {

    public BeforeOnlinePayDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void getOfflinePayInfo(String shopId) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("method", "BeforeOnlinePay");
        map.put("shop_id", shopId);
        OkHttpUtil.getInstance().get("", map, new MgCallback(BeforeOnlinePayBean.class) {

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.d(tag, responseBody);
            }

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                BeforeOnlinePayBean bean = (BeforeOnlinePayBean)responseBody;
                if(isEmpty(bean)){
                    getListener().getOfflinePayInfoError(BASE_ERROR_MESSAGE);
                    return;
                }
                if(bean.getStatusCode() != 200){
                    getListener().getOfflinePayInfoError(bean.getMessage());
                    return;
                }
                if(isEmpty(bean.getResult()) || isEmpty(bean.getResult().get(0)) || isEmpty(bean.getResult().get(0).getBody()) || isEmpty(bean.getResult().get(0).getBody().get(0))){
                    getListener().getOfflinePayInfoError(BASE_ERROR_MESSAGE);
                    return;
                }
                getListener().getOfflinePayInfoSuccess(bean.getResult().get(0).getBody().get(0));

            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                Log.d(tag, "onErrorResponse : " + message);
            }

        });
    }

    @Override
    public BeforeOnlinePayView getListener() {
        return (BeforeOnlinePayView)baseView;
    }
}
