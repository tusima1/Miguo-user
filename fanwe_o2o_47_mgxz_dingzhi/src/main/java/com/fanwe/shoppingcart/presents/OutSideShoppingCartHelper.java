package com.fanwe.shoppingcart.presents;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.base.Presenter;
import com.fanwe.base.Root;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.fanwe.utils.SDFormatUtil;

import java.util.List;
import java.util.TreeMap;

/**
 * 购物车模块。
 * Created by Administrator on 2016/8/18.
 */
public class OutSideShoppingCartHelper extends Presenter {

    /**
     * 回调。
     */
    private CallbackView mCallbackView;

    public OutSideShoppingCartHelper(CallbackView mCallbackView){
        this.mCallbackView = mCallbackView;
    }

    /**
     *
     * @param fx_user_id 分销用户id（可不给）
     * @param lgn_user_id 用户id
     * @param token 用户授权令牌
     * @param goods_id 商品ID
     * @param cart_type 商品类型“1” 为团购
     * @param add_goods_num 商品数量。
     */
    public void addShopCart(String fx_user_id,String lgn_user_id,String token,String goods_id,String  cart_type,String add_goods_num){
        TreeMap<String, String> params = new TreeMap<String,String>();
        params.put("fx_user_id", fx_user_id);
        params.put("lgn_user_id", lgn_user_id);
        params.put("token", App.getInstance().getToken());
        params.put("goods_id", goods_id);
        params.put("cart_type", cart_type);
        params.put("add_goods_num", add_goods_num);
        params.put("method", ShoppingCartconstants.SHOPPING_CART);
        OkHttpUtils.getInstance().post(null,params,new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {

                Root root = JSON.parseObject(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                    mCallbackView.onSuccess(ShoppingCartconstants.SHOPPING_CART_ADD, null);
                } else {
                    mCallbackView.onFailue(message);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }
    public void getUserShopCartList(){
        TreeMap<String, String> params = new TreeMap<String,String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", ShoppingCartconstants.SHOPPING_CART);
        OkHttpUtils.getInstance().get(null,params,new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {

                Root root = JSON.parseObject(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                    mCallbackView.onSuccess(ShoppingCartconstants.SHOPPING_CART_LIST, null);
                } else {
                    mCallbackView.onFailue(message);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }
    public void doDeleteShopCart(String id){
        TreeMap<String, String> params = new TreeMap<String,String>();
        params.put("token", App.getInstance().getToken());
        params.put("id",id);
        params.put("method", ShoppingCartconstants.SHOPPING_CART);
        OkHttpUtils.getInstance().delete(null,params,new MgCallback(){

            @Override
            public void onSuccessResponse(String responseBody) {

                Root root = JSON.parseObject(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if(ShoppingCartconstants.RESULT_OK.equals(statusCode)){
                    mCallbackView.onSuccess(ShoppingCartconstants.SHOPPING_CART_DELETE,null);
                }else{
                    mCallbackView.onFailue(message);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 批量加入购物车。
     * @param datas
     */
    public void multiAddShopCart(List<ShoppingCartInfo> datas){
        if(datas==null||datas.size()<1){
            return;
        }
        StringBuffer fx_user_ids =new StringBuffer();
        StringBuffer goods_ids =new StringBuffer();
        StringBuffer cart_types =new StringBuffer();
        StringBuffer add_goods_num =new StringBuffer();
        int size = datas.size();
        for(int i = 0 ; i < size ; i++){
            ShoppingCartInfo info = new ShoppingCartInfo();
            if(TextUtils.isEmpty(info.getId())||TextUtils.isEmpty(info.getNumber())){
                continue;
            }

            if(SDFormatUtil.stringToInteger(info.getNumber())<1){
                continue;
            }
            fx_user_ids.append(info.getFx_user_id()==null?"":info.getFx_user_id()+",");
            goods_ids.append(info.getId()+",");
            cart_types.append("1,");
            add_goods_num.append(info.getNumber()+",");
        }
        String values = goods_ids.toString();
        if(TextUtils.isEmpty(values)||values.length()<1){
            return ;
        }

        TreeMap<String, String> params = new TreeMap<String,String>();
        params.put("token", App.getInstance().getToken());
        params.put("fx_user_ids",fx_user_ids.substring(0,fx_user_ids.length()-1));
        params.put("goods_ids",goods_ids.substring(0,fx_user_ids.length()-1));
        params.put("cart_types",cart_types.substring(0,fx_user_ids.length()-1));
        params.put("add_goods_num",add_goods_num.substring(0,fx_user_ids.length()-1));
        params.put("method", ShoppingCartconstants.BATCH_SHOPPING_CART);
        OkHttpUtils.getInstance().delete(null,params,new MgCallback(){

            @Override
            public void onSuccessResponse(String responseBody) {

                Root root = JSON.parseObject(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if(ShoppingCartconstants.RESULT_OK.equals(statusCode)){
                    mCallbackView.onSuccess(ShoppingCartconstants.SHOPPING_CART_DELETE,null);
                }else{
                    mCallbackView.onFailue(message);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });


    }
    @Override
    public void onDestory() {
        mCallbackView = null;

    }
}
