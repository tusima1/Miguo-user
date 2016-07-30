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

import com.fanwe.app.App;
import com.fanwe.base.Result;
import com.fanwe.library.utils.MD5Util;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.o2o.miguo.R;
import com.google.gson.Gson;
import com.miguo.live.model.LiveUserSigEntity;
import com.miguo.live.model.RoomIDEntity;
import com.miguo.live.model.TestModel;
import com.miguo.live.views.customviews.MGToast;
import com.tencent.av.sdk.AVContext;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.presenters.LoginHelper;
import com.tencent.qcloud.suixinbo.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class LiveTestActivity extends Activity implements View.OnClickListener {
    private final int REQUEST_PHONE_PERMISSIONS = 0;

    private String mShop_id="4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b1";

    private String mToken="";
    private String mRoomId="";
    private String mUserSig="";
    private String mUser_id="";
    private LoginHelper mTencentLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_test2);
        checkPermission();
        getDeviceId();
        MGToast.showToast("Version:" + AVContext.getVersion());
        findViewById(R.id.bt_zhibo).setOnClickListener(this);
        findViewById(R.id.bt_login).setOnClickListener(this);
        findViewById(R.id.bt_get_sig).setOnClickListener(this);
        findViewById(R.id.bt_login_tls).setOnClickListener(this);
        findViewById(R.id.bt_enter).setOnClickListener(this);

//        init();
        //腾讯登录
        mTencentLogin = new LoginHelper(this);
//        TIMManager.getInstance().init(this);

    }

    private void init() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_zhibo) {
            doGetRoomId(mShop_id);
//            init();
        } else if (id == R.id.bt_login) {
                //18667932385
            doLogin(MD5Util.MD5("123456"), "8618957510999");
        }else if (id == R.id.bt_get_sig) {
            //获取腾讯usersig
            doGetUserSig();
        }else if (id == R.id.bt_login_tls) {
            //登录腾讯tls
//            mTencentLogin.tlsLogin("18667932385",MD5Util.MD5("123456"));
            mTencentLogin.imLogin(mUser_id,mUserSig);
        }else if (id==R.id.bt_enter){
            enterAVRoom();
        }
    }

    /**
     * 登录
     * @param s
     * @param s1
     */
    private void doLogin(String s, String s1) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("pwd", s);
        params.put("mobile", s1);
        params.put("method", "UserLogin");
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {


            @Override
            public void onErrorResponse(String message, String errorCode) {

            }

            @Override
            public void onSuccessListResponse(List<Result> resultList) {
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Gson gson=new Gson();
                TestModel testModel = gson.fromJson(responseBody, TestModel.class);
                mToken=testModel.getToken();
                mUser_id=testModel.getResult().get(0).getBody().get(0).getUser_id();
                MGToast.showToast("mToken"+mToken+"mUser_id"+mUser_id);
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
        params.put("token",mToken);
        params.put("method", "ApplyRoom");
        OkHttpUtils.getInstance().post(null, params, new MgCallback() {


            @Override
            public void onSuccessListResponse(List<Result> resultList) {

            }

            @Override
            public void onErrorResponse(String message, String errorCode) {

            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Gson gson=new Gson();
                RoomIDEntity testModel = gson.fromJson(responseBody, RoomIDEntity.class);
                mToken=testModel.getToken();
                mRoomId=testModel.getRoom_id();
                MGToast.showToast("mRoomId"+mRoomId);
            }
        });
    }

    /**
     * 获取UserSig
     *
     */
    public void doGetUserSig() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token",mToken);
        params.put("method", "GenerateSign");
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {


            @Override
            public void onSuccessListResponse(List<Result> resultList) {

            }

            @Override
            public void onErrorResponse(String message, String errorCode) {

            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Gson gson=new Gson();
                LiveUserSigEntity testModel = gson.fromJson(responseBody, LiveUserSigEntity.class);
                mToken=testModel.getToken();
                mUserSig=testModel.getResult().get(0).getBody().get(0).getUsersig();
                MySelfInfo.getInstance().setUserSig(mUserSig);
                MGToast.showToast("mUserSig"+mUserSig);
            }
        });
    }

    private void enterAVRoom(){
//如果是自己
        MySelfInfo.getInstance().setId(mUser_id);
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
