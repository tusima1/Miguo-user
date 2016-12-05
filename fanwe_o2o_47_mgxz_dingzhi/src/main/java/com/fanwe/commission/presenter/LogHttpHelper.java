package com.fanwe.commission.presenter;

import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.base.OldCallbackHelper;
import com.fanwe.commission.model.CommissionConstance;
import com.fanwe.commission.model.getCommissionLog.ResultCommissionLog;
import com.fanwe.commission.model.getCommissionLog.RootCommissionLog;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.google.gson.Gson;
import com.miguo.live.interf.IHelper;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by didik on 2016/8/28.
 * 日志模块
 */
public class LogHttpHelper extends OldCallbackHelper implements IHelper{

    private Gson gson;
    private CallbackView mView2;

    public LogHttpHelper(CallbackView mView2) {
        this.mView2=mView2;
        this.gson=new Gson();
    }


    /**
     * 资金日志
     * @param page 默认1
     * @param page_size 默认10
     */
    public void getCommissionLog(String page,String page_size,String select_type){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", CommissionConstance.COMMISSION_LOG);
        params.put("page", page);
        params.put("page_size", page_size);
        params.put("select_type", select_type);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {

                final List<ResultCommissionLog> result = gson.fromJson(responseBody, RootCommissionLog
                        .class).getResult();
                if (result!=null && result.size()>0){
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess(mView2,CommissionConstance.COMMISSION_LOG,result);
                        }
                    });
                }else {
                    onFailure2(mView2,CommissionConstance.COMMISSION_LOG);

                }
            }

            @Override
            public void onFinish() {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onFinish2(mView2,CommissionConstance.COMMISSION_LOG);
                    }
                });

            }
        });

    }
    /**
     * 佣金日志
     * @param page 默认1
     * @param page_size 默认10
     */
    public void getUserCommissionLog(String page,String page_size){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", CommissionConstance.USER_COMMISSION_LOG);
        params.put("page", page);
        params.put("page_size", page_size);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {

                final List<ResultCommissionLog> result = gson.fromJson(responseBody, RootCommissionLog
                        .class).getResult();
                if (result!=null && result.size()>0){
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess(mView2,CommissionConstance.USER_COMMISSION_LOG,result);
                        }
                    });
                }else {
                    onFailure2(mView2,CommissionConstance.USER_COMMISSION_LOG);
                }
            }

            @Override
            public void onFinish() {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onFinish2(mView2,CommissionConstance.USER_COMMISSION_LOG);

                    }
                });

            }
        });

    }


    @Override
    public void onDestroy() {
        mView2=null;
        gson=null;
    }
}
