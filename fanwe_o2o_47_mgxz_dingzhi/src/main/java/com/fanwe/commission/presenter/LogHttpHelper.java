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
 * 日志模块
 */
public class LogHttpHelper implements IHelper{

    private Gson gson;
    private CallbackView2 mView2;

    public LogHttpHelper(CallbackView2 mView2) {
        this.mView2=mView2;
        this.gson=new Gson();
    }


    /**
     * 资金日志
     * @param page 默认1
     * @param page_size 默认10
     */
    public void getCommissionLog(String page,String page_size){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", CommissionConstance.COMMISSION_LOG);
        params.put("page", page);
        params.put("page_size", page_size);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.e("test","responseBody:"+responseBody);
            }
        });
    }

    @Override
    public void onDestroy() {
        mView2=null;
        gson=null;
    }
}
