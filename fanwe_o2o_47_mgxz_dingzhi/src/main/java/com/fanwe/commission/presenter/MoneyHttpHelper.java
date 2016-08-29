package com.fanwe.commission.presenter;

import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.commission.model.CommissionConstance;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.google.gson.Gson;
import com.miguo.live.interf.IHelper;
import com.miguo.live.views.customviews.MGToast;

import java.util.TreeMap;

/**
 * Created by didik on 2016/8/28.
 * 提现(关于钱的部分)
 */
public class MoneyHttpHelper implements IHelper{

    private Gson gson;
    private CallbackView2 mView2;

    public MoneyHttpHelper(CallbackView2 mView2) {
        this.mView2=mView2;
        this.gson=new Gson();
    }


    /**
     * 资金日志
     * @param page 默认1
     * @param page_size 默认10
     */
    public void getGetBalance(String page,String page_size){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", CommissionConstance.COMMISSION_LOG);
        params.put("page", page);
        params.put("page_size", page_size);
        //TODO 资金日志

    }

    /**
     * 用户信息
     * 包括 手机号
     * 银行卡信息 等等
     */
    public void getUserAccount(){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", CommissionConstance.USER_ACCOUNT);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.e("Test","用户信息: "+responseBody);
//                gson.fromJson(responseBody,)
            }
        });
    }

    @Override
    public void onDestroy() {
        mView2=null;
        gson=null;
    }
}
