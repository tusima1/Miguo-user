package com.fanwe.network.okhttp3;

import android.text.TextUtils;
import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.base.ErrorCodeParse;
import com.fanwe.base.Root;
import com.fanwe.constant.EnumEventTag;
import com.fanwe.constant.ServerUrl;
import com.fanwe.user.model.UserCurrentInfo;
import com.sunday.eventbus.SDEventManager;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by didik 
 * Created time 2016/12/2
 * Description: 
 */

public abstract class OkCallback<T> extends AbsCallback<T> {
    @Override
    protected void parseResponse(Response response) {
        //before
        if (response == null || response.body() == null) {
            onFailure(method, new OkHttpException(OkHttpCode.RESPONSE_NULL, null));
            onAfter(method);
            return;
        }
        try {
            String body = response.body().string();

            if (ServerUrl.DEBUG) {
                Log.e("MIGUO method: "+method, body);
            }

            Root root = gson.fromJson(body, Root.class);
            String statusCode = root.getStatusCode();
            String token = root.getToken();
            int code = Integer.valueOf(statusCode);
            String message = checkMsg(root.getMessage(),code);

            if (code >= 200 && code <= 400) {
                //保存每个接口返回的token值 到缓存中。
                UserCurrentInfo userCurrentInfo = App.getInstance().getmUserCurrentInfo();
                if (!TextUtils.isEmpty(token) && !"null".equals(token)) {
                    userCurrentInfo.setToken(token);
                }
                if (code == 320 || code == 321) {
                    userCurrentInfo.setToken("");
                    SDEventManager.post(EnumEventTag.TOKEN_FAILUE.ordinal());
                } else {
                    //final parse to java bean
                    parseRealResponse(body);
                }
            } else {
                onFailure(method,new OkHttpException(OkHttpCode.ILLEGE_CODE,message));
            }
        } catch (Exception e) {
            onFailure(method,new OkHttpException(OkHttpCode.JSON_ERROR,e.getMessage()));
        }
        onAfter(method);
    }

    /**
     * 解析实体
     * @param body 实体
     */
    private void parseRealResponse(String body){
        ParameterizedType parameterizedType =  (ParameterizedType) getClass().getGenericSuperclass();
        Type[] types = parameterizedType.getActualTypeArguments();
        Class<T> beanClass = (Class<T>) types[0];
        if (beanClass == String.class){
            onSuccess(method, (T) body);
        }else {
            T t = gson.fromJson(body, beanClass);
            onSuccess(method,t);
        }
    }

    /**
     *  检查msg是否在本地的ErrorCode中.
     * @param oldMsg oldMsg
     * @param statusCode 状态码
     * @return 新的msg
     */
    private String checkMsg(String oldMsg,int statusCode){
        String newMessage = ErrorCodeParse.getErrorCodeMap().get(statusCode);
        if (!TextUtils.isEmpty(newMessage) && !"null".equals
                (newMessage) && !"404".equals(statusCode)) {
            return newMessage;
        }
        return oldMsg;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if(e.getCause().equals(SocketTimeoutException.class) ) {//超时
            onFailure(method,new OkHttpException(OkHttpCode.NETWORK_TIME_OUT,e.getMessage()));
        }
        onAfter(method);
    }

    @Override
    protected void onBefore(String method) {
        onBefore(method, tag);
    }

    @Override
    protected void onBefore(String method, Object tag) {

    }

    @Override
    protected void onSuccess(String method, T bean, Object tag) {

    }

    @Override
    protected void onFailure(String method, OkHttpException error) {
        onFailure(method, error, tag);
    }

    @Override
    protected void onFailure(String method, OkHttpException error, Object tag) {

    }

    @Override
    protected void onAfter(String method) {
        onAfter(method, tag);
    }

    @Override
    protected void onAfter(String method, Object tag) {

    }
}
