package com.fanwe.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.fanwe.app.App;
import com.fanwe.base.ErrorCodeParse;
import com.fanwe.base.Root;
import com.fanwe.constant.ServerUrl;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.user.model.UserCurrentInfo;
import com.google.gson.Gson;
import com.miguo.live.views.customviews.MGToast;
import com.sunday.eventbus.SDEventManager;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/22.
 */
public abstract class MgCallback<T> implements Callback {
    private static String TAG = MgCallback.class.getSimpleName();

    private Class clz;

    public MgCallback(Class clz) {
        this.clz = clz;
    }

    public MgCallback() {
        this.clz = Root.class;
    }

    public void onStart() {
        SDDialogManager.showProgressDialog("请稍候...");
    }


    public void onFinish() {
        SDDialogManager.dismissProgressDialog();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        onFinish();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

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
                String message = root.getMessage();
                String newMessage = ErrorCodeParse.getErrorCodeMap().get(statusCode);
                if (newMessage != null && !TextUtils.isEmpty(newMessage) && !"null".equals(newMessage) && !"404".equals(statusCode)) {
                    message = newMessage;
                }
                if (code >= 200 && code <= 400) {
                    //保存每个接口返回的token值 到缓存中。
                    UserCurrentInfo userCurrentInfo = App.getInstance().getmUserCurrentInfo();
                    if (!TextUtils.isEmpty(token) && !"null".equals(token)) {
                        userCurrentInfo.setToken(token);
                    }
                    if (code == 320 || code == 321) {
                        MGToast.showToast(message);
                        userCurrentInfo.setToken("");
                        SDEventManager.post(EnumEventTag.TOKEN_FAILUE.ordinal());
                    } else {
                        if(clz != Root.class){
                            Object bean = new Gson().fromJson(body, clz);
                            onSuccessResponseOnMainThreadWithBean(bean);
                        }else {
                            onSuccessResponseOnMainThread(body);
                        }
                    }
                } else {
                    onErrorResponseOnMainThread(message, statusCode);
                }

            } catch (Exception e) {
                 Log.e(TAG, e.getMessage());
                MGToast.showToast(e.getMessage());
            }
        }
        onFinish();
    }

    private void onSuccessResponseOnMainThread(final String responseBody){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                onSuccessResponse(responseBody);
            }
        });
    }
    private void onSuccessResponseOnMainThreadWithBean(final Object responseBody){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                onSuccessResponseWithBean(responseBody);
            }
        });
    }

    private void onErrorResponseOnMainThread(final String message,final String errorCode){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                onErrorResponse(message, errorCode);
            }
        });
    }

    public void onSuccessResponse(String responseBody) {
    }

    public void onSuccessResponseWithBean(Object responseBody) {
    }



    /**
     * 判断BODY对象是否存在。
     *
     * @param root
     * @return
     */
    public T validateBody(Root<T> root) {

        if (root.getResult() != null && root.getResult().size() > 0 && root.getResult().get(0) != null && root.getResult().get(0).getBody() != null && root.getResult().get(0).getBody().size() > 0) {
            return root.getResult().get(0).getBody().get(0);
        }
        return null;

    }

    public List<T> validateBodyList(Root<T> root) {

        if (root.getResult() != null && root.getResult().size() > 0 && root.getResult().get(0) != null && root.getResult().get(0).getBody() != null && root.getResult().get(0).getBody().size() > 0) {
            return root.getResult().get(0).getBody();
        }
        return null;

    }

    public abstract void onErrorResponse(String message, String errorCode);
}
