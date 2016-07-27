package com.miguo.live.views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.fanwe.base.Result;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.customviews.MGToast;
import com.tencent.av.sdk.AVContext;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Response;

public class LiveTestActivity extends Activity implements View.OnClickListener {
    private final int REQUEST_PHONE_PERMISSIONS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_test2);
        checkPermission();

        MGToast.showToast("Version:" + AVContext.getVersion());
        findViewById(R.id.bt_zhibo).setOnClickListener(this);
        findViewById(R.id.bt_login).setOnClickListener(this);
//        init();

    }

    private void init() {
        Intent intent = new Intent(this, LiveActivity.class);
        intent.putExtra(Constants.ID_STATUS, Constants.HOST);
        MySelfInfo.getInstance().setIdStatus(Constants.HOST);
        MySelfInfo.getInstance().setJoinRoomWay(true);
        CurLiveInfo.setTitle("直播");
        CurLiveInfo.setHostID(MySelfInfo.getInstance().getId());
        CurLiveInfo.setRoomNum(MySelfInfo.getInstance().getMyRoomNum());
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_zhibo) {
            doGetRoomId("wulala");
//            init();
        } else if (id == R.id.bt_login) {
            doLogin("654321", "13567100270");
        }
    }

    private void doLogin(String s, String s1) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("pwd", s);
        params.put("mobile", s1);
        params.put("method", "UserLogin");
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                MGToast.showToast("成功");
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                super.onResponse(call, response);
                MGToast.showToast("onResponse");
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                MGToast.showToast("onFailure");
            }

            @Override
            public void onStart() {
                super.onStart();
                MGToast.showToast("onStart");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                MGToast.showToast("onFinish");
            }
        });

    }

    public void doGetRoomId(String shop_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("shop_id", shop_id);
        params.put("method", "ApplyRoom");
        OkHttpUtils.getInstance().post(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                MGToast.showToast("成功");
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                super.onResponse(call, response);
                MGToast.showToast("onResponse");
            }

            @Override
            public void onSuccessResponse(Result responseBody) {

            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                MGToast.showToast("onFailure");
            }

            @Override
            public void onStart() {
                super.onStart();
                MGToast.showToast("onStart");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                MGToast.showToast("onFinish");
            }
        });
    }

    void checkPermission() {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.READ_PHONE_STATE);
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionsList.size() != 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_PHONE_PERMISSIONS);
            }
        }
    }


}
