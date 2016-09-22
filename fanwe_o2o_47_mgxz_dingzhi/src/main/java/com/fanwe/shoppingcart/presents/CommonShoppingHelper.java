package com.fanwe.shoppingcart.presents;

import com.fanwe.base.Presenter;
import com.fanwe.base.Root;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.shoppingcart.RefreshCalbackView;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.PaymentTypeInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.imcore.Context;

import java.lang.reflect.Type;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/8/23.
 */
public class CommonShoppingHelper extends Presenter {
    private RefreshCalbackView mCallbackView;
    private Context mContext;

    public CommonShoppingHelper(Context mContext,RefreshCalbackView mCallbackView){
        this.mContext = mContext;
        this.mCallbackView = mCallbackView;

    }
    public CommonShoppingHelper(RefreshCalbackView mCallbackView){
        this.mCallbackView = mCallbackView;

    }

    /**
     * 获取支付方式。
     */
    public void getPayment(){
        TreeMap<String, String> params = new TreeMap<String,String>();
        params.put("scene","android");
        params.put("method", ShoppingCartconstants.GET_PAYMENT);

        OkHttpUtils.getInstance().get(null,params,new MgCallback(){

            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<PaymentTypeInfo>>() {
                }.getType();
                Gson gson = new Gson();
                Root<PaymentTypeInfo> root = gson.fromJson(responseBody, type);

                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if(ShoppingCartconstants.RESULT_OK.equals(statusCode)){
                    List<PaymentTypeInfo> datas = validateBodyList(root);
                    mCallbackView.onSuccess(ShoppingCartconstants.GET_PAYMENT,datas);
                }else{
                    mCallbackView.onFailue(ShoppingCartconstants.GET_PAYMENT,message);
                }
            }
            @Override
            public void onErrorResponse(String message, String errorCode) {
                mCallbackView.onFailue(ShoppingCartconstants.GET_PAYMENT,message);
            }
        });
    }
    @Override
    public void onDestory() {

    }
}
