package com.fanwe.dao.barry.impl;

import com.alibaba.fastjson.JSON;
import com.fanwe.app.App;
import com.fanwe.base.Root;
import com.fanwe.dao.barry.ShappingCartDao;
import com.fanwe.dao.barry.view.ShappingCartDaoView;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.views.customviews.MGToast;

import java.util.TreeMap;

/**
 * Created by Administrator on 2016/10/12.
 */
public class ShappingCartDaoImpl implements ShappingCartDao{

    ShappingCartDaoView listener;


    public ShappingCartDaoImpl(ShappingCartDaoView listener) {
        this.listener = listener;
    }

    @Override
    public void addSaleToShappingCart(String roomId, String fx_user_id, String lgn_user_id, String goods_id, String cart_type, String add_goods_num) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("roomId", roomId);
        params.put("fx_user_id", fx_user_id);
        params.put("lgn_user_id", lgn_user_id);
        params.put("goods_id", goods_id);
        params.put("cart_type", cart_type);
        params.put("add_goods_num", add_goods_num);
        params.put("token", App.getInstance().getToken());
        params.put("method", LiveConstants.SHOPPING_CART);

        OkHttpUtil.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                Root root = JSON.parseObject(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if(LiveConstants.RESULT_SUCCESS.equals(statusCode)){
                    MGToast.showToast("添加购物车成功");
                    if(getListener()!=null){
                        getListener().addToShappingCartSuccess();
                    }
                }else{
                    if(getListener()!=null) {
                        getListener().addToShappingCartError(message);
                    }
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
                getListener().addToShappingCartError(message);
            }
        });
    }

    public ShappingCartDaoView getListener(){
        return listener;
    }

}
