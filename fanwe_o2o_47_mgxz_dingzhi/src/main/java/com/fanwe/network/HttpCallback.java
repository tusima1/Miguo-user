package com.fanwe.network;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.fanwe.app.App;
import com.fanwe.base.Root;
import com.fanwe.constant.ServerUrl;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.user.model.UserInfoNew;
import com.miguo.utils.MGUIUtil;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by didik on 2016/11/10.
 */

public abstract class HttpCallback<T> implements Callback {

    private static final String TAG = "HttpCallback";

    public void onSuccess(String response){

    }

    public void onFail(String msg,String errorCode){

    }
    public void onStart() {

    }

    public abstract void onFinish();
    public void onSuccessWithBean(T t){

    }


    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        dispatch(RequestCode.START);
        if (response == null || response.body() == null) {
            onFailure(call, new IOException());
        } else {
            String body = response.body().string();

            if (ServerUrl.DEBUG) {
                Log.e(TAG, body);
            }
            try {
                Root root = JSON.parseObject(body, Root.class);
                String statusCode = root.getStatusCode();
                String token = root.getToken();
                int code = Integer.valueOf(statusCode);
                if (code >= 200 && code <= 400) {
                    //保存每个接口返回的token值 到缓存中。
                    if (!TextUtils.isEmpty(token) && !"null".equals(token)) {
                        UserInfoNew userCurrentInfo = App.getApplication().getCurrentUser();
                        userCurrentInfo.setToken(token);
                    }
                    dispatch(RequestCode.SUCCESS,body);
                    try {
                        Class<T> clz ;
                        Type type = SDOtherUtil.getType(getClass(), 0);
                        if (type instanceof Class) {
                            clz = (Class<T>) type;
                            T t = JSON.parseObject(body, clz);
                            dispatch(RequestCode.SUCCESS_BEAN,t);
                        }
                    } catch (Exception e) {
                        //empty
                        Log.e(TAG,"parse body to json bean fail");
                    }
                } else {
                    String message = root.getMessage();
                    dispatch(RequestCode.FAIL,message,statusCode);
                }
            } catch (Exception e) {
                 Log.e(TAG, e.getMessage());
            }
        }
        dispatch(RequestCode.FINISH);
    }

    private void dispatch(final int who, final Object... var){
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (who){
                    case RequestCode.START:
                        onStart();
                        break;
                    case RequestCode.SUCCESS:
                        onSuccess(getString(var[0]));
                        break;
                    case RequestCode.FAIL:
                        onFail(getString(var[0]),getString(var[1]));
                        break;
                    case RequestCode.FINISH:
                        onFinish();
                        break;
                    case RequestCode.SUCCESS_BEAN:
                        onSuccessWithBean((T) var[0]);
                        break;
                    default:
                        onFinish();
                        break;
                }
            }
        });
    }

    private String getString(Object o){
        return o == null ? "": o.toString();
    }
}
