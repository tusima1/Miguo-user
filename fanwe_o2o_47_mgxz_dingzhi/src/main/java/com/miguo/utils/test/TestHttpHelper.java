package com.miguo.utils.test;

import com.fanwe.base.CallbackView2;

import java.util.TreeMap;

/**
 * Created by didik on 2016/9/9.
 */
public class TestHttpHelper extends MGHttpHelper {

    protected CallbackView2 mView2;

    public TestHttpHelper(CallbackView2 mView2) {
        this.mView2=mView2;
    }

    @Override
    protected TreeMap addParams(String method,TreeMap params) {
        return null;
    }

    @Override
    protected void onSuccess(String method, String responseBody) {

    }

    @Override
    protected void onFinished(String method) {

    }

}
