package com.fanwe.base;

import com.miguo.utils.MGLog;
import com.miguo.utils.MGUIUtil;

import java.util.List;

/**
 * Created by didik on 2016/11/3.
 */

public class BaseCallbackHelper {

    private static final int SUCCESS=0;
    private static final int FAILURE=1;
    private static final int FINISH=2;
    protected void onSuccess(CallbackView view, String method, List data){
        handResult(view,SUCCESS,method,data);
    }
    protected void onSuccess(CallbackView view, String responseBody){
        handResult(view,SUCCESS,responseBody,null);
    }
    protected void onFailure2(CallbackView view, String responseBody){
        handResult(view,FAILURE,responseBody,null);
    }

    protected void onFinish(CallbackView view2, String method){
        handResult(view2,FINISH,method,null);
    }
    private void handResult(final Object o, final int who, final String method, final List data){
        if (o==null){
            MGLog.e("callback is null!");
            return;
        }
        if (o instanceof CallbackView){
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (who){
                        case SUCCESS:
                            ((CallbackView) o).onSuccess(method,data);
                            break;
                        case FAILURE:
                            ((CallbackView) o).onFailue(method);
                            break;
                        case FINISH:
                            break;
                    }
                }
            });

        }else if (o instanceof CallbackView){
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (who){
                        case SUCCESS:
                            ((CallbackView) o).onSuccess(method,data);
                            break;
                        case FAILURE:
                            ((CallbackView) o).onFailue(method);
                            break;
                        case FINISH:
                            ((CallbackView) o).onFinish(method);
                            break;
                    }
                }
            });
        }
    }

}
