package com.fanwe.network;

import java.util.List;

/**
 * Created by didik on 2016/11/10.
 */

public abstract class FinalCallbackWithTag<T> extends FinalCallback<T> {
    @Override
    public void onFinish(String method){

    }
    public void onSuccess(String method, String responseBody,Object tag){}
    public void onSuccess(String method, List<T> data, Object tag){}
    public void onFail(String method,String msg,String errorCode,Object tag){}
    public abstract void onFinish(String method,Object tag);
}
