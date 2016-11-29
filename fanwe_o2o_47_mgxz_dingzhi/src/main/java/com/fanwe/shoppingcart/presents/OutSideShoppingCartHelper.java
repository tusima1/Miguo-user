package com.fanwe.shoppingcart.presents;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.base.Presenter;
import com.fanwe.base.Root;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.shoppingcart.RefreshCalbackView;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.OrderDetailInfo;
import com.fanwe.shoppingcart.model.ShoppingBody;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getUserRedpackets.ModelUserRedPacket;
import com.fanwe.utils.SDFormatUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
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
    private RefreshCalbackView mCallbackView;
    private CallbackView2 callbackView2;

    public OutSideShoppingCartHelper(RefreshCalbackView mCallbackView) {
        this.mCallbackView = mCallbackView;
    }

    public OutSideShoppingCartHelper(CallbackView2 mCallbackView) {
        this.callbackView2 = mCallbackView;
    }


    /**
     * 不要红包
     *
     * @param order_id 订单id
     */
    public void getOrderOperator(String order_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.ORDER_OPERATOR);
        params.put("order_id", order_id);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                if(mCallbackView == null) {
                    return;
                }
                Root root = JSON.parseObject(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                if ("200".endsWith(statusCode)) {
                    mCallbackView.onSuccess(UserConstants.ORDER_OPERATOR_GET, null);
                } else {
                    mCallbackView.onFailue(UserConstants.ORDER_OPERATOR_GET);
                }
            }
        });
    }

    /**
     * 添加到购物车。
     *
     * @param fx_user_id    分销用户id（可不给）
     * @param lgn_user_id   用户id
     * @param token         用户授权令牌
     * @param goods_id      商品ID
     * @param cart_type     商品类型“1” 为团购
     * @param add_goods_num 商品数量。
     * @param share_record_id  分享id
     */
    public void addShopCart(String fx_user_id, String lgn_user_id, String token, String goods_id, String cart_type, String add_goods_num,String share_record_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("fx_user_id", fx_user_id);
        params.put("lgn_user_id", lgn_user_id);
        params.put("token", App.getInstance().getToken());
        params.put("goods_id", goods_id);
        params.put("cart_type", cart_type);
        params.put("add_goods_num", add_goods_num);
        params.put("share_record_id", share_record_id);
        params.put("method", ShoppingCartconstants.SHOPPING_CART);
        OkHttpUtils.getInstance().post(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                if(mCallbackView == null){
                    return;
                }
                try {
                    Root root = JSON.parseObject(responseBody, Root.class);
                    String statusCode = root.getStatusCode();
                    String message = root.getMessage();
                    if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                        if (mCallbackView != null) {
                            mCallbackView.onSuccess(ShoppingCartconstants.SHOPPING_CART_ADD, null);
                        }
                    } else {
                        if (mCallbackView != null) {
                            mCallbackView.onFailue(message);
                        }
                    }
                }catch (Exception e){
                    Log.e("exception",e.getLocalizedMessage());
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });

    }

    /**
     * 获取用户购物车列表。
     */
    public void getUserShopCartList() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", ShoppingCartconstants.SHOPPING_CART);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                if(mCallbackView == null) {
                    return;
                }
                Type type = new TypeToken<Root<ShoppingCartInfo>>() {
                }.getType();
                Gson gson = new Gson();
                Root<ShoppingCartInfo> root = gson.fromJson(responseBody, type);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                    List<ShoppingCartInfo> listModel = null;
                    if (root.getResult() != null && root.getResult().size() > 0 && root.getResult().get(0) != null && root.getResult().get(0).getBody() != null && root.getResult().get(0).getBody().size() > 0) {
                        listModel = root.getResult().get(0).getBody();
                    }
                    if(mCallbackView!=null) {
                        mCallbackView.onSuccess(ShoppingCartconstants.SHOPPING_CART_LIST, listModel);
                    }
                } else {
                    if(mCallbackView!=null) {
                        mCallbackView.onFailue(ShoppingCartconstants.SHOPPING_CART_LIST, message);
                    }
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                if(mCallbackView == null) {
                    return;
                }
                mCallbackView.onFailue(ShoppingCartconstants.SHOPPING_CART_LIST, message);

            }
        });

    }

    /**
     * 删除某件商品。
     *
     * @param id
     */
    public void doDeleteShopCart(String id, final ShoppingCartInfo model) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("id", id);
        params.put("method", ShoppingCartconstants.SHOPPING_CART);
        OkHttpUtils.getInstance().delete(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                if(mCallbackView == null) {
                    return;
                }
                Root root = JSON.parseObject(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                    List<ShoppingCartInfo> datas = new ArrayList<ShoppingCartInfo>();
                    datas.add(model);
                    if(mCallbackView!=null) {
                        mCallbackView.onSuccess(ShoppingCartconstants.SHOPPING_CART_DELETE, datas);
                    }
                } else {
                    if(mCallbackView!=null) {
                        mCallbackView.onFailue(message);
                    }
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });

    }




    /**
     * 批量加入购物车。
     *
     * @param datas
     * @param fromShopCart  是否来自购物车，如果来自购物车的话取goods_id 要取 pro_id.。否则来自本地购物车的话取id
     *
     */
    public void multiAddShopCart(List<ShoppingCartInfo> datas,boolean fromShopCart) {
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

             String pro_id = "";
            if(fromShopCart){
                pro_id = info.getPro_id();
            }else{
                pro_id= info.getId();
            }
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
        params.put("share_record_ids", share_record_ids.substring(0, add_goods_num.length() - 1));
        params.put("method", ShoppingCartconstants.BATCH_SHOPPING_CART);
        OkHttpUtils.getInstance().post(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                if(mCallbackView == null) {
                    return;
                }
                Root root = JSON.parseObject(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if (mCallbackView != null) {
                    if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                        if(mCallbackView==null){
                            return;
                        }
                        mCallbackView.onSuccess(ShoppingCartconstants.BATCH_SHOPPING_CART, null);
                    } else {
                        if(mCallbackView==null){
                            return;
                        }
                        mCallbackView.onFailue(ShoppingCartconstants.BATCH_SHOPPING_CART, message);
                    }
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                if(mCallbackView==null){
                    return;
                }
                    mCallbackView.onFailue(ShoppingCartconstants.BATCH_SHOPPING_CART, message);

            }
        });


    }

    /**
     * 购物车结算按钮.
     *
     * @param good_ids
     */
    public void getDingdanDetail(String good_ids) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("id", good_ids);
        params.put("method", ShoppingCartconstants.SP_CART_TOORDER);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                if(mCallbackView == null) {
                    return;
                }
                Type type = new TypeToken<Root<ShoppingBody>>() {
                }.getType();
                Gson gson = new Gson();
                Root<ShoppingBody> root = gson.fromJson(responseBody, type);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                    if (root != null && root.getResult() != null && root.getResult().get(0) != null && root.getResult().get(0).getBody() != null) {
                        ShoppingBody shoppingBody = (ShoppingBody) root.getResult().get(0).getBody().get(0);
                        List<ShoppingBody> datas = new ArrayList<ShoppingBody>();
                        datas.add(shoppingBody);
                        if(mCallbackView == null) {
                            return;
                        }
                        mCallbackView.onSuccess(ShoppingCartconstants.SP_CART_TOORDER_GET, datas);
                    } else {
                        if(mCallbackView == null) {
                            return;
                        }
                        mCallbackView.onSuccess(ShoppingCartconstants.SP_CART_TOORDER_GET, null);
                    }
                } else {
                    if(mCallbackView == null) {
                        return;
                    }
                    mCallbackView.onFailue(ShoppingCartconstants.SP_CART_TOORDER_GET, message);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                if(mCallbackView == null) {
                    return;
                }
                mCallbackView.onFailue(ShoppingCartconstants.SP_CART_TOORDER_GET, message);
            }
        });
    }

    /**
     * 订单ID取订单详情。
     *
     * @param id 订单ID.
     */
    public void getDindanDetailById(String id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("id", id);
        params.put("method", ShoppingCartconstants.PENDIING_ORDER);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                if(mCallbackView == null) {
                    return;
                }
                Type type = new TypeToken<Root<ShoppingBody>>() {
                }.getType();
                Gson gson = new Gson();
                Root<ShoppingBody> root = gson.fromJson(responseBody, type);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                    if (root != null && root.getResult() != null && root.getResult().get(0) != null && root.getResult().get(0).getBody() != null) {
                        ShoppingBody shoppingBody = (ShoppingBody) root.getResult().get(0).getBody().get(0);
                        List<ShoppingBody> datas = new ArrayList<ShoppingBody>();
                        datas.add(shoppingBody);
                        mCallbackView.onSuccess(ShoppingCartconstants.SP_CART_TOORDER_GET, datas);
                    } else {
                        mCallbackView.onSuccess(ShoppingCartconstants.SP_CART_TOORDER_GET, null);
                    }
                } else {
                    mCallbackView.onFailue(ShoppingCartconstants.SP_CART_TOORDER_GET, message);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                if(mCallbackView == null) {
                    return;
                }
                mCallbackView.onFailue(ShoppingCartconstants.SP_CART_TOORDER_GET, message);
            }
        });

    }

    /**
     * 购物车生成订单。
     *
     * @param deals                商品IDS./
     * @param is_use_account_money 是否使用余额，1：使用，0：不使用 0或1
     * @param red_packet_ids       红包ids，逗号切割
     * @param payment              支付方式，0：未选，1：支付宝，2：微信
     * @param content              留言信息
     */
    public void createOrder(String deals, String is_use_account_money, String red_packet_ids, String payment, String content) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("deals", deals);
        params.put("is_use_account_money", is_use_account_money);
        params.put("red_packet_ids", red_packet_ids);
        params.put("payment", payment);
        params.put("content", content);
        params.put("method", ShoppingCartconstants.ORDER_INFO);

        OkHttpUtils.getInstance().put(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                if(mCallbackView == null) {
                    return;
                }
                Type type = new TypeToken<Root<OrderDetailInfo>>() {
                }.getType();
                Gson gson = new Gson();
                Root<OrderDetailInfo> root = gson.fromJson(responseBody, type);

                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                    List<OrderDetailInfo> datas = validateBodyList(root);

                    mCallbackView.onSuccess(ShoppingCartconstants.ORDER_INFO_CREATE, datas);
                } else {
                    mCallbackView.onFailue(ShoppingCartconstants.ORDER_INFO_CREATE, message);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                if(mCallbackView == null) {
                    return;
                }
                mCallbackView.onFailue(ShoppingCartconstants.ORDER_INFO_CREATE, message);
            }
        });
    }

    public void getOrderInfo(String id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("id", id);
        params.put("method", ShoppingCartconstants.GET_ORDER_INFO);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                if(mCallbackView == null) {
                    return;
                }
                Type type = new TypeToken<Root<OrderDetailInfo>>() {
                }.getType();
                Gson gson = new Gson();
                Root<OrderDetailInfo> root = gson.fromJson(responseBody, type);

                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                    List<OrderDetailInfo> datas = validateBodyList(root);
                    mCallbackView.onSuccess(ShoppingCartconstants.GET_ORDER_INFO, datas);
                } else {
                    mCallbackView.onFailue(ShoppingCartconstants.GET_ORDER_INFO, message);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                if(mCallbackView == null) {
                    return;
                }
                mCallbackView.onFailue(ShoppingCartconstants.GET_ORDER_INFO, message);
            }
        });
    }

    /**
     * 取优惠金额。
     *
     * @param id     商品ID
     * @param red_id 红包 ID
     */
    public void getBenefitCount(String id, String red_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("id", id);
        params.put("red_id", red_id);

        params.put("method", ShoppingCartconstants.SP_CART_TOORDER);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                if(mCallbackView == null) {
                    return;
                }
                Type type = new TypeToken<Root<HashMap<String, String>>>() {
                }.getType();
                Gson gson = new Gson();
                Root<HashMap<String, String>> root = gson.fromJson(responseBody, type);

                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                    List<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();
                    HashMap<String, String> map = (HashMap<String, String>) validateBody(root);
                    if (map != null) {
                        datas.add(map);
                    }
                    if(mCallbackView == null) {
                        return;
                    }
                       mCallbackView.onSuccess(ShoppingCartconstants.SP_CART_TOORDER_POST, datas);

                } else {
                    if(mCallbackView == null) {
                        return;
                    }
                        mCallbackView.onFailue(ShoppingCartconstants.SP_CART_TOORDER_POST, message);

                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                if(mCallbackView == null) {
                    return;
                }
                mCallbackView.onFailue(ShoppingCartconstants.SP_CART_TOORDER_POST, message);
            }
        });
    }

    /**
     * 通过商品IDS 取红包列表。
     *
     * @param id 商品IDS。
     */
    public void getUsableRedPacketList(String id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("id", id);
        params.put("method", ShoppingCartconstants.GET_USERING_REDPACKETS);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                if(callbackView2 == null) {
                    return;
                }
                Type type = new TypeToken<Root<ModelUserRedPacket>>() {
                }.getType();
                Gson gson = new Gson();
                Root<ModelUserRedPacket> root = gson.fromJson(responseBody, type);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                    List<ModelUserRedPacket> datas = validateBodyList(root);
                    callbackView2.onSuccess(ShoppingCartconstants.GET_USERING_REDPACKETS, datas);
                } else {
                    callbackView2.onFailue(message);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                if(callbackView2 == null) {
                    return;
                }
                callbackView2.onFailue(message);
            }

            @Override
            public void onFinish() {
                if(callbackView2 == null) {
                    return;
                }
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callbackView2.onFinish(UserConstants.USER_RED_PACKET_LIST);
                    }
                });
            }
        });
    }

    /**
     * 用订单号重新取支付详情。
     *
     * @param order_id             订单id
     * @param payment              支付方式
     * @param is_use_account_money 是否使用余额
     */
    public void payByOrderId(String order_id, String payment, String is_use_account_money) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("order_id", order_id);
        params.put("is_use_account_money", is_use_account_money);
        params.put("payment", payment);
        params.put("method", ShoppingCartconstants.ORDER_INFO);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                if(mCallbackView == null) {
                    return;
                }
                Type type = new TypeToken<Root<OrderDetailInfo>>() {
                }.getType();
                Gson gson = new Gson();
                Root<OrderDetailInfo> root = gson.fromJson(responseBody, type);

                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                    List<OrderDetailInfo> datas = validateBodyList(root);
                    mCallbackView.onSuccess(ShoppingCartconstants.ORDER_INFO_CREATE, datas);
                } else {
                    mCallbackView.onFailue(ShoppingCartconstants.ORDER_INFO_CREATE, message);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                if(mCallbackView == null) {
                    return;
                }
                mCallbackView.onFailue(ShoppingCartconstants.ORDER_INFO_CREATE, message);
            }
        });

    }

    @Override
    public void onDestory() {
        mCallbackView = null;
        callbackView2 = null;
    }
}
