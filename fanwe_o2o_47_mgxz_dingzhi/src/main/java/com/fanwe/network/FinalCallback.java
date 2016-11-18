package com.fanwe.network;

import java.util.List;

/**
 * Created by didik on 2016/11/10.
 */

public abstract class FinalCallback<T> {
    public void onStart(String method){}
    public void onSuccess(String method, String responseBody){}
    public void onSuccessWithBean(String method, List<T> data){}
    public void onFail(String method,String msg,String errorCode){}
    public abstract void onFinish(String method);
}
