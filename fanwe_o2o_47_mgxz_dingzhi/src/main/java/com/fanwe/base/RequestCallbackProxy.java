package com.fanwe.base;

import java.util.List;

/**
 * Created by didik on 2016/11/4.
 */

public abstract class RequestCallbackProxy implements IRequestCallback {
    @Override
    public void onStart(String method, String msg) {}
    @Override
    public abstract void onSuccess(String method, List data);
    public void onSuccess(String method, List data,Object tag){}
    @Override
    public void onFailure(String method, String msg) {}
    public void onFailue(String responseBody) {}
    @Override
    public void onFinish(String method, String msg){}
    public void onFinish(String method){}
    public void onSuccess(String responseBody){}
}
