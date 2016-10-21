package com.miguo.dao.impl;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.fanwe.app.App;
import com.fanwe.base.Root;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.google.gson.Gson;
import com.miguo.dao.HiShopDetailDao;
import com.miguo.entity.HiShopDetailBean;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.view.BaseView;
import com.miguo.view.HiShopDetailView;

import java.util.TreeMap;

/**
 * Created by Administrator on 2016/10/21.
 */
public class HiShopDetailDaoImpl extends BaseDaoImpl implements HiShopDetailDao{

    public HiShopDetailDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void getShopDetail(String shop_id, String m_longitude, String m_latitude) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("method", "ShopDetails");
        map.put("shop_id", shop_id);
        map.put("m_longitude", m_longitude);
        map.put("m_latitude", m_latitude);
        map.put("token", App.getApplication().getToken());
        OkHttpUtils.getInstance().get("", map, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.d(tag, "onSuccessResponse : " + responseBody);
                HiShopDetailBean bean = new Gson().fromJson(responseBody, HiShopDetailBean.class);
                if(bean != null){
                    if(bean.getStatusCode() == 200){
                        getListener().getShopDetailSuccess(bean.getResult().get(0));
                    }else {
                        getListener().getShopDetailError();
                    }
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                Log.d(tag, "onErrorResponse : " + message);
            }

        });
    }

    @Override
    public HiShopDetailView getListener() {
        return (HiShopDetailView)baseView;
    }
}
