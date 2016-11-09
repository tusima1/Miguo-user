package com.miguo.dao.impl;

import com.alibaba.fastjson.JSON;
import com.fanwe.app.App;
import com.fanwe.base.Root;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.miguo.dao.ShoppingCartDao;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.view.BaseView;
import com.miguo.view.ShoppingCartView;

import java.util.TreeMap;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/11/9.
 */
public class ShoppingCartDaoImpl extends BaseDaoImpl implements ShoppingCartDao{

    public ShoppingCartDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public ShoppingCartView getListener() {
        return (ShoppingCartView)baseView;
    }

    @Override
    public void addToShoppingCart(String roomId, String fx_user_id, String lgn_user_id, String goods_id, String cart_type, String add_goods_num) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("roomId", roomId);
        params.put("fx_user_id", fx_user_id);
        params.put("lgn_user_id", lgn_user_id);
        params.put("goods_id", goods_id);
        params.put("cart_type", cart_type);
        params.put("add_goods_num", add_goods_num);
        params.put("token", App.getInstance().getToken());
        params.put("method", LiveConstants.SHOPPING_CART);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                Root root = JSON.parseObject(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if(LiveConstants.RESULT_SUCCESS.equals(statusCode)){
                    getListener().addToShoppingCartSuccess();
                }else{
                    getListener().addToShoppingCartError();
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().addToShoppingCartError();
            }
        });
    }
}
