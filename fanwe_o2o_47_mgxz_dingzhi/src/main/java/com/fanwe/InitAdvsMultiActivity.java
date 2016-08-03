package com.fanwe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fanwe.adapter.InitAdvsPagerAdapter;
import com.fanwe.app.App;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.common.CommonInterface;
import com.fanwe.common.ImageLoaderManager;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.adapter.SDBasePagerAdapter.SDBasePagerAdapterOnItemClickListener;
import com.fanwe.library.customview.SDSlidingPlayView;
import com.fanwe.library.customview.SDSlidingPlayView.SDSlidingPlayViewOnTouchListener;
import com.fanwe.library.customview.SDViewPager.EnumMeasureMode;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.library.utils.SDTimer;
import com.fanwe.library.utils.SDTimer.SDTimerListener;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.InitActStart_pageModel;
import com.fanwe.model.Init_indexActModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.fanwe.work.RetryInitWorker;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.miguo.utils.MGLog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 初始化Activity
 */
public class InitAdvsMultiActivity extends BaseActivity {

    /**
     * 广告图片显示时间
     */
    private static long ADVS_DISPLAY_TIME = 3 * 1000;

    /**
     * 正常初始化成功后显示时间
     */
    private static long NORMAL_DISPLAY_TIME = 3 * 1000;

    private final int REQUEST_PHONE_PERMISSIONS = 0;

    private Button mBtn_skip;

    private SDSlidingPlayView mSpvAd;

    private InitAdvsPagerAdapter mAdapter;

    private SDTimer mTimer = new SDTimer();

    private long start;

    private String username;
    private int user_id;

    private SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_init_advs_multi);
        init();
    }

    private void init() {
        checkPermission();

        startStatistics();
        initTimer();
        registerClick();
        initSlidingPlayView();
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager
                        .PERMISSION_GRANTED) || (checkSelfPermission(Manifest.permission
                        .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    ADVS_DISPLAY_TIME = Integer.MAX_VALUE;
                    NORMAL_DISPLAY_TIME = Integer.MAX_VALUE;
                }else {
                    submmit();//只需要一次
                }
            }

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
        mSpvAd = (SDSlidingPlayView) findViewById(R.id.spv_content);
        mBtn_skip = (Button) findViewById(R.id.btn_skip);
    }


    private void registerClick() {
        mBtn_skip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });
    }

    private void initSlidingPlayView() {
        mSpvAd.mVpgContent.setmMeasureMode(EnumMeasureMode.NORMAL);
        mSpvAd.setmImageNormalResId(R.drawable.ic_main_dot2_normal);
        mSpvAd.setmImageSelectedResId(R.drawable.ic_main_dot2_foused);
        mSpvAd.setmListenerOnTouch(new SDSlidingPlayViewOnTouchListener() {

            @Override
            public void onUp(View v, MotionEvent event) {

            }

            @Override
            public void onTouch(View v, MotionEvent event) {

            }

            @Override
            public void onMove(View v, MotionEvent event) {
                if (mAdapter != null && mAdapter.getCount() > 1) {
                    mTimer.stopWork();
                }
            }

            @Override
            public void onDown(View v, MotionEvent event) {

            }
        });
    }

    private void requestInitInterface() {
        CommonInterface.requestInit(new SDRequestCallBack<Init_indexActModel>() {
            private boolean nSuccess = false;

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (actModel.getStatus() == 1) {
                    nSuccess = true;
                    dealInitSuccess(actModel);
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

    protected void dealInitSuccess(Init_indexActModel model) {
        List<InitActStart_pageModel> listModel = model.getStart_page_new();

        if (model.getQq_app_key().equals("")) {
            model.setQq_app_key("1101169715");
        }
        if (model.getQq_app_secret().equals("")) {
            model.setQq_app_secret("FtAZVvB6LZ85hjdE");
        }
        if (model.getWx_app_key().equals("")) {
            model.setWx_app_key("wx6aafdae1bac40206");
        }
        if (model.getWx_app_secret().equals("")) {
            model.setWx_app_secret("5f29a228760302af0d85774d02390273");
        }

        bindAdvsImages(listModel);
    }

    protected void bindAdvsImages(List<InitActStart_pageModel> listModel) {
        List<InitActStart_pageModel> listModelCached = findCachedModel(listModel);
        if (!SDCollectionUtil.isEmpty(listModelCached)) {
            mAdapter = new InitAdvsPagerAdapter(listModelCached, mActivity);
            mAdapter.setmListenerOnItemClick(new SDBasePagerAdapterOnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    InitActStart_pageModel model = mAdapter.getItemModel(position);
                    if (model != null) {
                        int type = model.getType();
                        Intent intent = AppRuntimeWorker.createIntentByType(type, model.getData()
                                , false);
                        if (intent != null) {
                            try {
                                mTimer.stopWork();
                                intent.putExtra(BaseActivity.EXTRA_IS_ADVS, true);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            mSpvAd.setAdapter(mAdapter);
            startAdvsDisplayTimer();
            SDViewUtil.show(mBtn_skip);
        } else {
            startNormalDisplayTimer();
        }
    }

    /**
     * 找到已经缓存过的实体
     *
     * @param listModel
     * @return
     */
    private List<InitActStart_pageModel> findCachedModel(List<InitActStart_pageModel> listModel) {
        List<InitActStart_pageModel> listCachedModel = new ArrayList<InitActStart_pageModel>();
        if (!SDCollectionUtil.isEmpty(listModel)) {
            for (InitActStart_pageModel model : listModel) {
                String url = model.getImg();
                if (ImageLoaderManager.isCacheExistOnDisk(url)) {
                    listCachedModel.add(model);
                } else {
                    ImageLoader.getInstance().loadImage(url, null);
                }
            }
        }
        return listCachedModel;
    }

    private void startAdvsDisplayTimer() {
        long now = System.currentTimeMillis();
        long past = now - start;
        if (past >= ADVS_DISPLAY_TIME) {
            startMainActivity();
            return;
        }
        mTimer.startWork(ADVS_DISPLAY_TIME - past, Long.MAX_VALUE, new SDTimerListener() {
            @Override
            public void onWorkMain() {
                startMainActivity();
            }

            @Override
            public void onWork() {

            }
        });
    }

    private void startNormalDisplayTimer() {
        long now = System.currentTimeMillis();
        long past = now - start;
        if (past >= NORMAL_DISPLAY_TIME) {
            startMainActivity();
            return;
        }
        mTimer.startWork(NORMAL_DISPLAY_TIME - past, Long.MAX_VALUE, new SDTimerListener() {
            @Override
            public void onWorkMain() {
                startMainActivity();
            }

            @Override
            public void onWork() {

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

    void checkPermission() {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager
                    .PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.READ_PHONE_STATE);
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionsList.size() != 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_PHONE_PERMISSIONS);
            } else {
                //已经不是第一次,已经有权限
                //初始化百度sdk
                Log.e("init", "normal");
                initBaiduMap();
                getDeviceId();
            }
        }else {
            initBaiduMap();
            getDeviceId();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PHONE_PERMISSIONS) {
            if (grantResults.length != 0 && (checkSelfPermission(Manifest.permission
                    .READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED)) {
                //初始化百度sdk
                //这里只会走一次
                initBaiduMap();
                getDeviceId();
                submmit();//只需要一次
                MGLog.e("成功初始化!");
                startMainActivity();
            }
        }
    }
}
