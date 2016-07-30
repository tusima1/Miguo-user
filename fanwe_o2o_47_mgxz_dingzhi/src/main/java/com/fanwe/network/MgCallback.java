package com.fanwe.network;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.fanwe.app.App;
import com.fanwe.base.Result;
import com.fanwe.base.Root;
import com.fanwe.constant.ServerUrl;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDToast;
import com.fanwe.user.model.UserCurrentInfo;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/22.
 */
public abstract class MgCallback implements Callback {
    private static String TAG = MgCallback.class.getSimpleName();


    public void onStart() {
        SDDialogManager.showProgressDialog("请稍候...");
    }


    public void onFinish() {
        SDDialogManager.dismissProgressDialog();
    }

    @Override
    public void onFailure(Call call, IOException e) {
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        if (response == null || response.body() == null) {
            onFailure(call, new IOException());
        } else {
            String body = response.body().string();
            onSuccessResponse(body);
            if (ServerUrl.DEBUG) {
                Log.e(TAG, body);
            }
            try {
                Root root = JSON.parseObject(body, Root.class);
                String statusCode = root.getStatusCode();
                int code = Integer.valueOf(statusCode);
                if (code >= 200 && code <= 300) {
                    //保存每个接口返回的token值 到缓存中。
                    if (root.getToken() != null) {
                        String token = root.getToken();
                        UserCurrentInfo userCurrentInfo = App.getInstance().getmUserCurrentInfo();
                        userCurrentInfo.setToken(token);
                    }
                    if (root.getResult() != null) {
                        //root.getResult() 返回结果是一个JSONARRAY ,可以直接  JsonArray()解释。
                        onSuccessListResponse(root.getResult());
                    } else {
                        onSuccessResponse(body);
                    }
                } else {
                    String message = root.getMessage();
                    onErrorResponse(message, statusCode);
                }

            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
                SDToast.showToast(e.getMessage());
            }
        }
        onFinish();
    }
    public  abstract  void onSuccessListResponse(List<Result> resultList);
    public  void onSuccessResponse(String responseBody){
    }
    public abstract void onErrorResponse(String message,String errorCode);
}
