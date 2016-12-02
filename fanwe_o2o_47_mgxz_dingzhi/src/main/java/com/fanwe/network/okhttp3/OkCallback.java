package com.fanwe.network.okhttp3;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by didik 
 * Created time 2016/12/2
 * Description: 
 */

public class OkCallback<T> extends AbsCallback<T>{

    @Override
    protected void parseResponse(Response response) {

    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    protected void onBefore(String method) {

    }

    @Override
    protected void onBefore(String method, Object tag) {

    }

    @Override
    protected void onSuccess(String method, T bean) {

    }

    @Override
    protected void onSuccess(String method, T bean, Object tag) {

    }

    @Override
    protected void onFailure(String method, OkHttpException error) {

    }

    @Override
    protected void onFailure(String method, OkHttpException error, Object tag) {

    }

    @Override
    protected void onAfter(String method) {

    }

    @Override
    protected void onAfter(String method, Object tag) {

    }
}
