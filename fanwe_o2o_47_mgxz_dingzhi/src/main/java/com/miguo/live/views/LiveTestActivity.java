package com.miguo.live.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.fanwe.app.App;
import com.fanwe.base.Result;
import com.fanwe.library.utils.MD5Util;
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

    private String mShop_id="4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b1";

    private String mToken="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_test2);
        checkPermission();
        getDeviceId();
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
            doGetRoomId(mShop_id);
//            init();
        } else if (id == R.id.bt_login) {
            doLogin(MD5Util.MD5("123456"), "18667932385");
        }
    }

    /**
     * 登录
     * @param s
     * @param s1
     */
    private void doLogin(String s, String s1) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("pwd", s);
        params.put("mobile", s1);
        params.put("method", "UserLogin");
        OkHttpUtils.getInstance().get(null, params, new MgCallback<JSONObject>() {
            @Override
            public void onSuccessResponse(Result<JSONObject> responseBody) {
                MGToast.showToast(responseBody.getBody().get(0).getString("token"));
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {

            }

            @Override
            public void onSuccessResponse(String responseBody) {
                MGToast.showToast(responseBody+"哈哈");
            }
        });

    }

    /**
     * 获取房间号
     * @param shop_id
     */
    public void doGetRoomId(String shop_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("shop_id", shop_id);
        params.put("method", "ApplyRoom");
        OkHttpUtils.getInstance().post(null, params, new MgCallback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                super.onResponse(call, response);
                MGToast.showToast("onResponse");
            }

            @Override
            public void onSuccessResponse(Result responseBody) {
                MGToast.showToast("成功");
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {

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
    /**
     * 获取设备IMEI
     * 需要权限.6.0申请无效
     */
    public void getDeviceId(){
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        App.getInstance().setImei(telephonyManager.getDeviceId());
    }

}
