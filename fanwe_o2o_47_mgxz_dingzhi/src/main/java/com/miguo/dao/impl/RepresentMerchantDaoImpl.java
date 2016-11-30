package com.miguo.dao.impl;

import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.getRepresentMerchant.RootRepresentMerchant;
import com.google.gson.Gson;
import com.miguo.dao.RepresentMerchantDao;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.view.BaseView;
import com.miguo.view.RepresentMerchantView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by barry/ on 2016/10/24.
 */
public class RepresentMerchantDaoImpl extends BaseDaoImpl implements RepresentMerchantDao{

    public RepresentMerchantDaoImpl(BaseView baseView) {
        super(baseView);
    }


    @Override
    public void getRepresentMerchant(String ent_id, String shop_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getApplication().getToken());
        params.put("ent_id", ent_id);
        params.put("shop_id", shop_id);
        params.put("method", SellerConstants.REPRESENT_MERCHANT);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootRepresentMerchant root = new Gson().fromJson(responseBody, RootRepresentMerchant.class);
                if ("200".equals(root.getStatusCode())) {
                    List<RootRepresentMerchant> roots = new ArrayList<RootRepresentMerchant>();
                    roots.add(root);
                    getListener().getRepresentMerchantSuccess();
                } else
                {
                     onErrorResponse(root.getMessage(),root.getStatusCode());
                }
                getListener().onFinish();
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                String value = getErrorMessage(errorCode);
                if(!TextUtils.isEmpty(value)){
                    message = value;
                }
                MGToast.showToast(value);
                getListener().onFinish();
            }
        });

    }

    @Override
    public RepresentMerchantView getListener() {
        return (RepresentMerchantView)baseView;
    }
}
