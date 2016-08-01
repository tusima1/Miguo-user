package com.fanwe.network;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.fanwe.app.App;
import com.fanwe.base.Result;
import com.fanwe.base.Root;
import com.fanwe.constant.ServerUrl;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDToast;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.model.UserInfoNew;

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
                String token = root.getToken();
                int code = Integer.valueOf(statusCode);
                if (code >= 200 && code <= 400) {
                    //保存每个接口返回的token值 到缓存中。
                    if (!TextUtils.isEmpty(token)) {
                        UserCurrentInfo userCurrentInfo = App.getInstance().getmUserCurrentInfo();
                        userCurrentInfo.setToken(token);
                    }
                    onSuccessResponse(body);
                } else {
                    String message = root.getMessage();
                    onErrorResponse(message, statusCode);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                SDToast.showToast(e.getMessage());
            }
        }
        onFinish();
    }


    public void onSuccessResponse(String responseBody) {
    }

    /**
     * 判断BODY对象是否存在。
     * @param root
     * @return
     */
    public  T validateBody(Root<T> root) {

        if (root.getResult() != null && root.getResult().size() > 0 && root.getResult().get(0) != null && root.getResult().get(0).getBody() != null && root.getResult().get(0).getBody().size() > 0)
        {
            return root.getResult().get(0).getBody().get(0);
        }
        return null;

    }

    public abstract void onErrorResponse(String message, String errorCode);
}
