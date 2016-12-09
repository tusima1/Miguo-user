package com.miguo.dao.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.fanwe.app.App;
import com.fanwe.base.Root;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.fanwe.utils.SDFormatUtil;
import com.miguo.dao.ShoppingCartMultiAddDao;
import com.miguo.view.BaseView;
import com.miguo.view.ShoppingCartMultiAddView;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by zlh on 2016/12/6.
 */

public class ShoppingCartMultiAddDaoImpl extends BaseDaoImpl implements ShoppingCartMultiAddDao{

    public ShoppingCartMultiAddDaoImpl(BaseView baseView) {
        super(baseView);
    }

    private void multiAdd(List<ShoppingCartInfo> datas, boolean fromShopCart) {
        if (datas == null || datas.size() < 1) {
            return;
        }
        StringBuffer fx_user_ids = new StringBuffer();
        StringBuffer goods_ids = new StringBuffer();
        StringBuffer cart_types = new StringBuffer();
        StringBuffer add_goods_num = new StringBuffer();
        StringBuffer share_record_ids = new StringBuffer();
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            ShoppingCartInfo info = datas.get(i);
            String pro_id = fromShopCart ? info.getPro_id() : info.getId();
            if (TextUtils.isEmpty(pro_id) || TextUtils.isEmpty(info.getNumber())) {
                continue;
            }

            if (SDFormatUtil.stringToInteger(info.getNumber()) < 1) {
                continue;
            }
            fx_user_ids.append(info.getFx_user_id() == null ? "" : info.getFx_user_id() + ",");
            goods_ids.append(pro_id + ",");
            cart_types.append("1,");
            add_goods_num.append(info.getNumber() + ",");
            share_record_ids.append(info.getShare_record_id()+",");
        }
        String values = goods_ids.toString();
        if (TextUtils.isEmpty(values) || values.length() < 1) {
            return;
        }

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        if (!TextUtils.isEmpty(fx_user_ids.toString())) {
            params.put("fx_user_ids", fx_user_ids.substring(0, fx_user_ids.length() - 1));
        }
        params.put("goods_ids", goods_ids.substring(0, goods_ids.length() - 1));
        params.put("cart_types", cart_types.substring(0, cart_types.length() - 1));
        params.put("add_goods_num", add_goods_num.substring(0, add_goods_num.length() - 1));
        params.put("share_record_ids", share_record_ids.substring(0, share_record_ids.length() - 1));
        params.put("method", ShoppingCartconstants.BATCH_SHOPPING_CART);
        OkHttpUtils.getInstance().post(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                Root root = JSON.parseObject(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                    if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                        getListener().multiAddSuccess();
                        return;
                    }
                    getListener().multiAddError(message);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().multiAddError(message);
            }
        });
    }

    @Override
    public void multiAddFromShoppingCart(List<ShoppingCartInfo> datas) {
        multiAdd(datas, true);
    }

    @Override
    public void multiAddFromOther(List<ShoppingCartInfo> datas) {
        multiAdd(datas, false);
    }

    @Override
    public ShoppingCartMultiAddView getListener() {
        return (ShoppingCartMultiAddView)baseView;
    }
}
