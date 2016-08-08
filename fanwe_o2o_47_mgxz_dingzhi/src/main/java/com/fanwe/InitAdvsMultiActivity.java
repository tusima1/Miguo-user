package com.fanwe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import com.fanwe.app.App;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.common.CommonInterface;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.library.utils.SDTimer;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.Init_indexActModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.RetryInitWorker;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.miguo.utils.MGLog;
import com.miguo.utils.permission.DangerousPermissions;
import com.miguo.utils.permission.PermissionsHelper;

import cn.jpush.android.api.JPushInterface;

/**
 * 初始化Activity
 */
public class InitAdvsMultiActivity extends BaseActivity {

    // app所需要的全部危险权限
    static final String[] PERMISSIONS = new String[]{
            DangerousPermissions.CAMERA,
            DangerousPermissions.CONTACTS,
            DangerousPermissions.LOCATION,
            DangerousPermissions.MICROPHONE,
            DangerousPermissions.PHONE,
            DangerousPermissions.STORAGE
    };

    /**
     * 广告图片显示时间
     */
    private static long ADVS_DISPLAY_TIME = 3 * 1000;

    /**
     * 正常初始化成功后显示时间
     */
    private static long NORMAL_DISPLAY_TIME = 3 * 1000;

    private SDTimer mTimer = new SDTimer();

    private long start;

    private SharedPreferences setting;
    private PermissionsHelper permissionsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_init_advs_multi);
        if (Build.VERSION.SDK_INT>=23){
            checkPermissions();
        }else {
            init();
        }
    }
    private void checkPermissions() {
        permissionsHelper = new PermissionsHelper(this,PERMISSIONS);
        if (permissionsHelper.checkAllPermissions(PERMISSIONS)){
            permissionsHelper.onDestroy();
            //do nomarl
            init();
        }else {
            //申请权限
            permissionsHelper.startRequestNeedPermissions();
        }
        permissionsHelper.setonAllNeedPermissionsGrantedListener(new PermissionsHelper.onAllNeedPermissionsGrantedListener() {


            @Override
            public void onAllNeedPermissionsGranted() {
                MGLog.e("权限全部获取了!");
                init();
            }

            @Override
            public void onPermissionsDenied() {
                MGLog.e("权限被拒绝了!");
                InitAdvsMultiActivity.this.finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionsHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
//        initBaiduMap();
        startStatistics();
        initTimer();
        getDeviceId();
        requestInitInterface();
    }

    private void initBaiduMap() {
        BaiduMapManager.getInstance().init(App.getInstance().getApplicationContext());
    }

    /**
     * 获取设备IMEI
     * 需要权限.6.0申请无效
     */
    public void getDeviceId() {
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        App.getInstance().setImei(telephonyManager.getDeviceId());
    }

    private void startStatistics() {
        setting = getSharedPreferences("firstApp", Context.MODE_PRIVATE);
        Boolean user_first = setting.getBoolean("FIRST", true);
        String version = setting.getString("version", -1 + "");
        PackageInfo info = SDPackageUtil.getCurrentPackageInfo();
        String versionCode = String.valueOf(info.versionCode);
        if (user_first || (!versionCode.equals(-1 + "") && !version.equals(versionCode))) {// 第一次
            submmit();//只需要一次
            setting.edit().putBoolean("FIRST", false).commit();
            setting.edit().putString("version", versionCode);
        }
    }

    private void submmit() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mob_brand = Build.BRAND;

        String mob_model = Build.MODEL;

        String mob_imei = tm.getDeviceId();

        String sys_name = "Android";
        String sys_version = Build.VERSION.RELEASE;

        PackageInfo info = SDPackageUtil.getCurrentPackageInfo();
        RequestModel model = new RequestModel();
        model.putCtl("init");
        model.putAct("first_run");
        model.put("mob_brand", mob_brand);
        model.put("app_name", "mgxz");
        model.put("mob_model", mob_model);
        model.put("app_version", info.versionCode);
        model.put("mob_imei", mob_imei);
        model.put("sys_name", sys_name);
        model.put("sys_version", sys_version);
        InterfaceServer.getInstance().requestInterface(model, new SDRequestCallBack<BaseActModel>
                (false) {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (actModel.getStatus() == 1) {
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void initTimer() {
        start = java.lang.System.currentTimeMillis();
    }

    private void requestInitInterface() {
        CommonInterface.requestInit(new SDRequestCallBack<Init_indexActModel>() {
            private boolean nSuccess = false;

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (actModel.getStatus() == 1) {
                    nSuccess = true;
                    startMainActivity();
                }

            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {
                if (!nSuccess) {
                    startMainActivity();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                nSuccess = false;
                RetryInitWorker.getInstance().start(); // 如果初始化失败重试
            }
        });
    }

    private void startMainActivity() {
        // Intent intent = new Intent(getApplicationContext(),
        // GuideActivity.class);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        mTimer.stopWork();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
    }

}
