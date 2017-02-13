package com.miguo.utils.test;

import com.fanwe.app.App;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.google.gson.Gson;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;

import java.util.TreeMap;

/**
 * Created by didik on 2016/9/9.
 */
public abstract class MGHttpHelper {

    public static final int GET=0;
    public static final int POST=-1;
    public static final int PUT=-2;
    public static final int DELETE=-3;
    public static final int THIRD_LOGIN=-4;

    protected Gson gson;
    public MGHttpHelper(){initNeed();}

    protected void initNeed(){
        this.gson=new Gson();
    }

    public void doHttpMethod(String method, int mode){
        doHttpMethod(method,mode,null);
    }

    public void doHttpMethod(final String method, final int mode , String customUrl){
        TreeMap<String, String> params = new TreeMap<>();
        params.put("token", App.getInstance().getToken());
        params.put("method", method);
        //添加一些参数非必须参数
        addParams(method,params,mode);
        MgCallback callback=new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(final String responseBody) {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(method,responseBody,mode);
                    }
                });

            }

            @Override
            public void onFinish() {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onFinished(method,mode);
                    }
                });
            }
        };
        switch (mode){
            case GET:
                OkHttpUtil.getInstance().get(customUrl,params,callback);
                break;
            case POST:
                OkHttpUtil.getInstance().post(customUrl,params,callback);
                break;
            case PUT:
                OkHttpUtil.getInstance().put(customUrl,params,callback);
                break;
            case DELETE:
                OkHttpUtil.getInstance().delete(customUrl,params,callback);
                break;
            case THIRD_LOGIN:
                OkHttpUtil.getInstance().thirdUrlGet(customUrl,params,callback);
                break;
            default:
                OkHttpUtil.getInstance().get(null,params,callback);
                break;
        }

    }

    protected abstract TreeMap addParams(String method,TreeMap params,int mode);
    protected abstract void onSuccess(String method,String responseBody,int mode);
    protected abstract void onFinished(String method,int mode);
}
