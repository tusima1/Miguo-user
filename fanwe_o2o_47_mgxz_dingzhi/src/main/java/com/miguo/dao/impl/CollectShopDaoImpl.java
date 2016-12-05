package com.miguo.dao.impl;

import com.fanwe.app.App;
import com.fanwe.base.Root;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.seller.model.SellerConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguo.dao.CollectShopDao;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.view.BaseView;
import com.miguo.view.CollectShopView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/21.
 * 商家详情收藏接口实现类
 */
public class CollectShopDaoImpl extends BaseDaoImpl implements CollectShopDao{

    public CollectShopDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public CollectShopView getListener() {
        return (CollectShopView)baseView;
    }

    @Override
    public void collectShop(String merchantId) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getApplication().getToken());
        params.put("shop_id", merchantId);
        params.put("method", SellerConstants.SHOP_COLLECT);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {

                Type type = new TypeToken<Root<HashMap<String,String>>>() {
                }.getType();
                Gson gson = new Gson();
                Root<HashMap<String,String>> root = gson.fromJson(responseBody, type);
                String status = root.getStatusCode();
                if("200".equals(status)) {
                    HashMap<String, String> map = (HashMap<String, String>) validateBody(root);
                    getListener().collectSuccess(map);
                }else{
                    getListener().collectError();
                }

            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    @Override
    public void unCollectShop(String merchantId) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getApplication().getToken());
        params.put("shop_id", merchantId);
        params.put("method", SellerConstants.SHOP_COLLECT);

        OkHttpUtils.getInstance().delete(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                getListener().unCollectSuccess();
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }
}
