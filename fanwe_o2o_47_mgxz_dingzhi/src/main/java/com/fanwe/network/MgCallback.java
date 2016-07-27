package com.fanwe.network;

import android.util.Log;


import com.fanwe.constant.ServerUrl;
import com.fanwe.library.dialog.SDDialogManager;

import java.io.IOException;

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
            if (ServerUrl.DEBUG) {
                Log.e(TAG, body);
            }
            onSuccessResponse(body);
        }

    }

    public abstract void onSuccessResponse(String responseBody);
}
