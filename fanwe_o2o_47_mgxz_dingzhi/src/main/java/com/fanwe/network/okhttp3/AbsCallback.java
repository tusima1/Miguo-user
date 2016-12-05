package com.fanwe.network.okhttp3;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by didik 
 * Created time 2016/12/2
 * Description: 
 */

public abstract class AbsCallback<T> implements Callback {

    protected Object tag;
    protected String method;
    protected final Gson gson;
    public void setMethodTag(String method,Object tag) {
        this.tag = tag;
        this.method=method;
    }

    public AbsCallback() {
        this.gson = new Gson();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        parseResponse(response);
    }

    protected abstract void parseResponse(Response response);

    /********************* api ***********************/

    protected abstract void onBefore(String method);
    protected abstract void onBefore(String method,Object tag);
    protected abstract void onSuccess(String method,T bean);
    protected abstract void onSuccess(String method,T bean,Object tag);
    protected abstract void onFailure(String method,OkHttpException error);
    protected abstract void onFailure(String method,OkHttpException error,Object tag);
    protected abstract void onAfter(String method);
    protected abstract void onAfter(String method,Object tag);

}
