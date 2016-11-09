package com.fanwe.base;

import android.os.Looper;

import com.miguo.utils.MGLog;
import com.miguo.utils.MGUIUtil;

import java.util.List;

/**
 * Created by didik on 2016/11/3.
 */

public class OldCallbackHelper {

    private static final int SUCCESS=0;
    private static final int FAILURE=1;
    private static final int FINISH=2;
    protected void onSuccess(CallbackView view,String method,List data){
        handResult(view,SUCCESS,method,data);
    }
    protected void onSuccess(CallbackView2 view2,String method,List data){
        handResult(view2,SUCCESS,method,data);
    }
    protected void onSuccess(CallbackView view,String responseBody){
        handResult(view,SUCCESS,responseBody,null);
    }
    protected void onSuccess(CallbackView2 view2,String responseBody){
        handResult(view2,SUCCESS,responseBody,null);
    }
    protected void onFailure2(CallbackView view,String responseBody){
        handResult(view,FAILURE,responseBody,null);
    }
    protected void onFailure2(CallbackView2 view2,String responseBody){
        handResult(view2,FAILURE,responseBody,null);
    }

    protected void onFinish2(CallbackView2 view2, String method){
        handResult(view2,FINISH,method,null);
    }
    private void handResult(final Object o, final int who, final String method, final List data){
        if (o==null){
            MGLog.e("callback is null!");
            return;
        }
        if (o instanceof CallbackView){
            if (Looper.getMainLooper()==Looper.myLooper()){
                dispatchData((CallbackView) o,who,method,data);
            }else {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dispatchData((CallbackView) o,who,method,data);
                    }
                });
            }
        }else if (o instanceof CallbackView2){
            if (Looper.getMainLooper()==Looper.myLooper()){
                dispatchData2((CallbackView2) o,who,method,data);
            }else {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dispatchData2((CallbackView2) o,who,method,data);
                    }
                });
            }
        }
    }

    private void dispatchData(CallbackView view,int who,String method,List data){
        switch (who){
            case SUCCESS:
                view.onSuccess(method,data);
                break;
            case FAILURE:
                view.onFailue(method);
                break;
            case FINISH:
                break;
        }
    }
    private void dispatchData2(CallbackView2 view2,int who,String method,List data){
        switch (who){
            case SUCCESS:
                view2.onSuccess(method,data);
                break;
            case FAILURE:
                view2.onFailue(method);
                break;
            case FINISH:
                view2.onFinish(method);
                break;
        }
    }

}