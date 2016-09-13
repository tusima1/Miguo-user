package com.fanwe.reward.presenters;

import com.fanwe.app.App;
import com.fanwe.base.Presenter;
import com.fanwe.base.Root;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.reward.RewardConstants;
import com.fanwe.reward.model.DiamondTypeEntity;
import com.fanwe.reward.model.DiamondUserOwnEntity;
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
 * Created by Administrator on 2016/9/11.
 */
public class DiamondHelper  extends Presenter {
    private RefreshCalbackView mCallbackView;
    private Context mContext;

    public DiamondHelper(Context mContext,RefreshCalbackView mCallbackView){
        this.mContext = mContext;
        this.mCallbackView = mCallbackView;

    }
    public DiamondHelper(RefreshCalbackView mCallbackView){
        this.mCallbackView = mCallbackView;
    }


    public void GetDiamondList(){
        TreeMap<String, String> params = new TreeMap<String,String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", RewardConstants.BUY_DIAMOND);

        OkHttpUtils.getInstance().get(null,params,new MgCallback(){

            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<DiamondTypeEntity>>() {
                }.getType();
                Gson gson = new Gson();
                Root<DiamondTypeEntity> root = gson.fromJson(responseBody, type);

                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if(ShoppingCartconstants.RESULT_OK.equals(statusCode)){
                    List<DiamondTypeEntity> datas = validateBodyList(root);
                    mCallbackView.onSuccess(RewardConstants.BUY_DIAMOND,datas);
                }else{
                    mCallbackView.onFailue(RewardConstants.BUY_DIAMOND,message);
                }
            }
            @Override
            public void onErrorResponse(String message, String errorCode) {
                mCallbackView.onFailue(ShoppingCartconstants.GET_PAYMENT,message);
            }
        });
    }

    public void getUserDiamond(){
        TreeMap<String, String> params = new TreeMap<String,String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", RewardConstants.USER_DIAMOND);

        OkHttpUtils.getInstance().get(null,params,new MgCallback(){

            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<DiamondUserOwnEntity>>() {
                }.getType();
                Gson gson = new Gson();
                Root<DiamondUserOwnEntity> root = gson.fromJson(responseBody, type);

                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if(ShoppingCartconstants.RESULT_OK.equals(statusCode)){
                    List<DiamondUserOwnEntity> datas = validateBodyList(root);
                    mCallbackView.onSuccess(RewardConstants.USER_DIAMOND,datas);
                }else{
                    mCallbackView.onFailue(RewardConstants.USER_DIAMOND,message);
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
        mCallbackView = null;
        mContext = null;
    }
}
