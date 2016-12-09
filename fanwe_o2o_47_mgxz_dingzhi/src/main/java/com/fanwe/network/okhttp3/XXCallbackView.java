package com.fanwe.network.okhttp3;

/**
 * Created by didik 
 * Created time 2016/12/5
 * Description: 
 */

public abstract class XXCallbackView implements ICallbackView {
    @Override
    public void onBefore(String method) {

    }

    @Override
    public void onAfter(String method) {

    }

    @Override
    public void onFailure(String method, OkHttpException error) {

    }
    protected void onBefore(String method, Object tag){

    }
    protected void onSuccess(String method,Object bean,Object tag){}
    protected void onFailure(String method,OkHttpException error,Object tag){}
    protected void onAfter(String method,Object tag){}
}
