package com.fanwe.network;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.fanwe.app.App;
import com.fanwe.base.ErrorCodeParse;
import com.fanwe.base.Root;
import com.fanwe.constant.ServerUrl;
import com.fanwe.constant.EnumEventTag;
import com.fanwe.user.model.UserCurrentInfo;
import com.google.gson.Gson;
import com.miguo.utils.MGUIUtil;
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
    private Object tag;

    public MgCallback(Class clz) {
        this.clz = clz;
    }
    public MgCallback() {
        this.clz = Root.class;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void onStart() {
    }

    public void onFinishResponse() {
        //TODO finish 回调一定会走,没有网络也会.
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onFinish();
            }
        });

    }
    public void onFinish() {

    }

    @Override
    public void onFailure(Call call, IOException e) {
        onFinishResponse();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        onStart();
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
                Long sysTime = root.getTimestamp();
                if (newMessage != null && !TextUtils.isEmpty(newMessage) && !"null".equals(newMessage) && !"404".equals(statusCode)) {
                    message = newMessage;
                }
                if(sysTime!=0){
                    App.getInstance().setSysTime(sysTime);
                }
                if (code >= 200 && code <= 400) {
                    //保存每个接口返回的token值 到缓存中。
                    UserCurrentInfo userCurrentInfo = App.getInstance().getmUserCurrentInfo();
                    if (!TextUtils.isEmpty(token) && !"null".equals(token)) {
                        userCurrentInfo.setToken(token);
                    }
                    if (code == 320 || code == 321) {
//                        MGToast.showToast(message);
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
                onFailure(call,null);
            }
        }
        onFinishResponse();
    }

    private void onSuccessResponseOnMainThread(final String responseBody){
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onSuccessResponse(responseBody);
            }
        });
    }
    private void onSuccessResponseOnMainThreadWithBean(final Object responseBody){
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onSuccessResponseWithBean(responseBody);
            }
        });
    }

    public void onErrorResponseOnMainThread(final String message,final String errorCode){
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onErrorResponse(message, errorCode);
            }
        });
    }

    public void onSuccessResponse(String responseBody){

    }

    public void onSuccessResponseWithBean(Object responseBody) {
    }



    /**
     * 判断BODY对象是否存在。
     *
     * @param root
     * @return
     */
    protected T validateBody(Root<T> root) {

        if (root.getResult() != null && root.getResult().size() > 0 && root.getResult().get(0) != null && root.getResult().get(0).getBody() != null && root.getResult().get(0).getBody().size() > 0) {
            return root.getResult().get(0).getBody().get(0);
        }
        return null;

    }

    protected List<T> validateBodyList(Root<T> root) {

        if (root.getResult() != null && root.getResult().size() > 0 && root.getResult().get(0) != null && root.getResult().get(0).getBody() != null && root.getResult().get(0).getBody().size() > 0) {
            return root.getResult().get(0).getBody();
        }
        return null;

    }

    public String getErrorMessage(String errorCode){
        String changeMessage = ErrorCodeParse.getErrorCodeMap().get(errorCode);
        return  changeMessage;
    }
public void onErrorResponse(String message, String errorCode){}
}
