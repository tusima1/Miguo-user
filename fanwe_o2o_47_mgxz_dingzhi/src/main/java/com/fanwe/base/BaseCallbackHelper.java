package com.fanwe.base;

import com.miguo.utils.MGUIUtil;

import java.util.List;

/**
 * Created by didik on 2016/11/3.
 */

public abstract class BaseCallbackHelper {
//    void onSuccess(String responseBody);
//
//    void onSuccess(String method, List datas);
//
//    void onFailue(String responseBody);
//
//
//    void onFinish(String method);
    protected void onSuccess(CallbackView view,String method,List data){

    }
    protected void onSuccess(CallbackView2 view2,String method,List data){

    }

    protected void onSuccess(CallbackView view,String responseBody){

    }
    protected void onSuccess(CallbackView2 view2,String responseBody){

    }
    protected void onFailure(CallbackView view,String responseBody){

    }
    protected void onFailure(CallbackView2 view2,String responseBody){

    }

    protected void onFinish(final CallbackView2 view2, final String method){
        if (view2!=null){
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view2.onFinish(method);
                }
            });
        }
    }

}
