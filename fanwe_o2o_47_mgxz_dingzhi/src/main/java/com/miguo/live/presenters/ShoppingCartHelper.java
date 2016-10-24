package com.miguo.live.presenters;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.base.CallbackView2;
import com.fanwe.base.Presenter;
import com.fanwe.base.Root;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.views.customviews.MGToast;

import java.util.TreeMap;

/**
 * 直播中购物车处理。
 * Created by Administrator on 2016/8/15.
 */
public class ShoppingCartHelper extends Presenter {

    public Context mContext;
    private CallbackView mView;
    private CallbackView2 mView2;

    public ShoppingCartHelper(Context mContext,CallbackView mView){
        this.mContext = mContext;

        this.mView = mView;
    }

    public ShoppingCartHelper(CallbackView2 mView2){
        this.mView2 = mView2;
    }

    /**添加至购物车
     * @param roomId    直播房间号id,可不给（直播时须给）
     * @param fx_user_id  分销用户id（可不给）
     * @param lgn_user_id  用户id
     * @param goods_id  商品id（目前指的就是团购id）
     * @param cart_type  商品类型（目前只有团购，给“1”）
     * @param add_goods_num  往购物车里添加的数量，不是指希望购物车达到的数量
     */
    public void addToShoppingCart(String roomId,String fx_user_id,String lgn_user_id,String goods_id,String cart_type,String add_goods_num){

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
                    MGToast.showToast("添加购物车成功");
                    if(mView!=null){
                        mView.onSuccess(LiveConstants.SHOPPING_CART, null);
                    }else if (mView2!=null){
                        mView2.onSuccess(LiveConstants.SHOPPING_CART, null);
                    }
                }else{
                    if(mView!=null) {
                        mView.onFailue(message);
                    }else if (mView2!=null){
                        mView2.onFailue(LiveConstants.SHOPPING_CART);
                    }
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
                mView.onFailue(message);
            }
        });
    }

    public void addToShoppingCart2(String roomId,String fx_user_id,String lgn_user_id,String goods_id,String cart_type,String add_goods_num){

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
                    MGToast.showToast("添加购物车成功");
                    if(mView!=null){
                        mView.onSuccess(ShoppingCartconstants.SHOPPING_CART_ADD, null);
                    }else if (mView2!=null){
                        mView2.onSuccess(ShoppingCartconstants.SHOPPING_CART_ADD, null);
                    }
                }else{
                    if(mView!=null) {
                        mView.onFailue(message);
                    }else if (mView2!=null){
                        mView2.onFailue(ShoppingCartconstants.SHOPPING_CART_ADD);
                    }
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
                mView.onFailue(message);
            }
        });
    }

    /**
     * 取购物车列表。
     */
    public void getShoppingCartList(){
        TreeMap<String, String> params = new TreeMap<String, String>();

        params.put("token", App.getInstance().getToken());
        params.put("method", LiveConstants.SHOPPING_CART);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                Root root = JSON.parseObject(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if(LiveConstants.RESULT_SUCCESS.equals(statusCode)){
                    MGToast.showToast("添加购物车成功。");
                    if(mView!=null){
                        mView.onSuccess(LiveConstants.SHOPPING_CART, null);
                    }
                }else{
                    if(mView!=null) {
                        mView.onFailue(message);
                    }
                }

            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
                mView.onFailue(message);
            }
        });
    }
    @Override
    public void onDestory() {

    }
}
