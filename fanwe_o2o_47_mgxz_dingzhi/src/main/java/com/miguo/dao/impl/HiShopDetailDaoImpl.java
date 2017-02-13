package com.miguo.dao.impl;

import android.text.TextUtils;
import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.google.gson.Gson;
import com.miguo.dao.HiShopDetailDao;
import com.miguo.entity.HiShopDetailBean;
import com.miguo.view.BaseView;
import com.miguo.view.HiShopDetailView;

import java.util.TreeMap;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/21.
 * 商家详情页接口实现类
 */
public class HiShopDetailDaoImpl extends BaseDaoImpl implements HiShopDetailDao{

    public HiShopDetailDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void getShopDetail(String shop_id, String m_longitude, String m_latitude) {
        if(TextUtils.isEmpty(shop_id)){
            getListener().getShopDetailError();
            return;
        }
        TreeMap<String, String> map = new TreeMap<>();
        map.put("method", "ShopDetailsNew");
        map.put("shop_id", shop_id);
        map.put("m_longitude", m_longitude);
        map.put("m_latitude", m_latitude);
        map.put("token", App.getApplication().getToken());
        OkHttpUtil.getInstance().get("", map, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
               // Log.d(tag, "onSuccessResponse : " + responseBody);
                HiShopDetailBean bean = new Gson().fromJson(responseBody, HiShopDetailBean.class);
                if(bean != null){
                    if(bean.getStatusCode() == 200){
                        if(getListener()!=null) {
                            getListener().getShopDetailSuccess(bean.getResult().get(0));
                        }
                    }else {
                        if(getListener()!=null) {
                            getListener().getShopDetailError();
                        }
                    }
                }else{
                    if(getListener()!=null) {
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
