package com.fanwe.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.fanwe.BaseActivity;
import com.fanwe.MainActivity;
import com.fanwe.common.ImageLoaderManager;
import com.fanwe.constant.ServerUrl;
import com.fanwe.dao.LocalUserModelDao;
import com.fanwe.dao.SettingModelDao;
import com.fanwe.event.EnumEventTag;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.library.SDLibrary;
import com.fanwe.library.command.SDCommandManager;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.config.SDLibraryConfig;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.RuntimeConfigModel;
import com.fanwe.model.SettingModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.model.UserInfoNew;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;
import com.sunday.eventbus.SDEventObserver;
import com.ta.util.netstate.TANetChangeObserver;
import com.ta.util.netstate.TANetWorkUtil.netType;
import com.ta.util.netstate.TANetworkStateReceiver;
import com.tencent.qcloud.suixinbo.presenters.InitBusinessHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


public class App extends Application implements SDEventObserver, TANetChangeObserver {

    private static App mApp = null;

    public List<Class<? extends BaseActivity>> mListClassNotFinishWhenLoginState0 = new ArrayList<Class<? extends BaseActivity>>();
    public RuntimeConfigModel mRuntimeConfig = new RuntimeConfigModel();
    public Intent mPushIntent;

    public LocalUserModel getmLocalUser() {
        return LocalUserModelDao.queryModel();
    }

    /**
     * 当前用户信息。存在于内存中。定义到2016-7-27 by  zhouhy
     */
    public UserCurrentInfo mUserCurrentInfo;
    protected String imei;

    /**
     * 自我引用 .
     */
    private static App myApplication;
    /**
     * 是否腾讯IM 认证成功 。
     */
    public boolean imLoginSuccess = false;
    /**
     * 是否初始化AV.
     */
    public boolean isAvStart = false;
    /**
     * 当前用户的昵称。
     */
    public String nickName;

    public void setmLocalUser(LocalUserModel localUser) {
        if (localUser != null) {
            LocalUserModelDao.insertModel(localUser);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

        init();
    }

    private void init() {
        mApp = this;
        ImageLoaderManager.initImageLoader();
        initSDLibrary();

//		initAppCrashHandler();
        //关闭友盟分析默认的统计方式
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setCatchUncaughtExceptions(true);

        //腾讯视频sdk
        InitBusinessHelper.initApp(this);


        //初始化友盟分享
        UmengShareManager.initConfig();

        SDEventManager.register(this);
        TANetworkStateReceiver.registerObserver(this);
        initSettingModel();
        initJPush();
        addClassesNotFinishWhenLoginState0();
        SDCommandManager.getInstance().initialize();
        LogUtil.isDebug = ServerUrl.DEBUG;

        mUserCurrentInfo = UserCurrentInfo.getInstance();
    }


    private void initJPush() {
        JPushInterface.init(this);
        JpushHelper.registerAll();
    }

    private void initSDLibrary() {
        SDLibrary.getInstance().init(getApplication());

        SDLibraryConfig config = new SDLibraryConfig();

        config.setmMainColor(getResources().getColor(R.color.main_color));
        config.setmMainColorPress(getResources().getColor(R.color.main_color_press));

        config.setmTitleColor(getResources().getColor(R.color.bg_title_bar));
        config.setmTitleColorPressed(getResources().getColor(R.color.bg_title_bar_pressed));
        config.setmTitleHeight(getResources().getDimensionPixelOffset(R.dimen.height_title_bar));

        config.setmStrokeColor(getResources().getColor(R.color.stroke));
        config.setmStrokeWidth(SDViewUtil.dp2px(1));

        config.setmCornerRadius(getResources().getDimensionPixelOffset(R.dimen.corner));
        config.setmGrayPressColor(getResources().getColor(R.color.gray_press));

        SDLibrary.getInstance().initConfig(config);
    }

    private void addClassesNotFinishWhenLoginState0() {
        mListClassNotFinishWhenLoginState0.add(MainActivity.class);
    }

//    private void initAppCrashHandler() {
//        if (!ServerUrl.DEBUG) {
//            CrashHandler crashHandler = CrashHandler.getInstance();
//            crashHandler.init(getApplicationContext());
//        }
//    }

    private void initSettingModel() {
        // 插入成功或者数据库已经存在记录
        SettingModelDao.insertOrCreateModel(new SettingModel());
        mRuntimeConfig.updateIsCanLoadImage();
        mRuntimeConfig.updateIsCanPushMessage();
    }

    public static App getApplication() {
        return mApp;
    }

    public void exitApp(boolean isBackground) {
        AppConfig.setRefId("");
        SDActivityManager.getInstance().finishAllActivity();
        SDEventManager.post(EnumEventTag.EXIT_APP.ordinal());
        if (isBackground) {

        } else {
            System.exit(0);
        }
    }

    public void clearAppsLocalUserModel() {

        AppConfig.setSessionId("");
        AppConfig.setRefId("");
        LocalUserModelDao.deleteAllModel();

    }

    public static String getStringById(int resId) {
        return getApplication().getString(resId);
    }

    @Override
    public void onConnect(netType type) {
        mRuntimeConfig.updateIsCanLoadImage();
    }

    @Override
    public void onDisConnect() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTerminate() {
        SDEventManager.unregister(this);
        super.onTerminate();
    }

    @Override
    public void onEvent(SDBaseEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEventBackgroundThread(SDBaseEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEventAsync(SDBaseEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        // TODO Auto-generated method stub

    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    /**
     * 返回单实例.
     *
     * @return HsApplication
     */
    public static App getInstance() {
        return myApplication;
    }

    public UserCurrentInfo getmUserCurrentInfo() {
        if(mUserCurrentInfo==null){
            mUserCurrentInfo = UserCurrentInfo.getInstance();
        }
        return mUserCurrentInfo;
    }

    public void setmUserCurrentInfo(UserCurrentInfo mUserCurrentInfo) {
        this.mUserCurrentInfo = mUserCurrentInfo;
    }

    public String getToken() {
        String token = "";
        if (this.mUserCurrentInfo != null) {
            UserInfoNew infoNew = mUserCurrentInfo.getUserInfoNew();
            if (infoNew != null) {
                token = mUserCurrentInfo.getToken();
            }
        }
        return token;
    }

    public String getUserSign() {

        String useSign = "";
        if (this.mUserCurrentInfo != null) {

            if (mUserCurrentInfo.getUserSign() != null) {
                useSign = mUserCurrentInfo.getUserSign();
            }
        }
        return useSign;

    }

    public void setUserSign(String useSign) {

        if (TextUtils.isEmpty(useSign)) {
            return;
        }
        if (this.mUserCurrentInfo != null) {

            mUserCurrentInfo.setUserSign(useSign);
        }


    }

    public String getUserNickName() {


        if (this.mUserCurrentInfo != null) {

            if (mUserCurrentInfo.getUserInfoNew() != null) {
                nickName = mUserCurrentInfo.getUserInfoNew().getNick();
                if (TextUtils.isEmpty(nickName) || "null".equals(nickName.trim())) {
                    nickName = mUserCurrentInfo.getUserInfoNew().getUser_name();
                }
                if (TextUtils.isEmpty(nickName) || "null".equals(nickName.trim())) {
                    nickName = mUserCurrentInfo.getUserInfoNew().getUser_id();
                }
            }
        }
        return nickName;

    }

    public String getUserIcon() {
        String icon = "";
        if (this.mUserCurrentInfo != null) {
            if (mUserCurrentInfo.getUserInfoNew() != null) {
                icon = mUserCurrentInfo.getUserInfoNew().getIcon();
            }
        }
        return icon;

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public void clearAllData(){
        this.imLoginSuccess = false;
        this.isAvStart = false;
        this.nickName = "";
        this.mUserCurrentInfo=null;

    }

    public boolean isImLoginSuccess() {
        return imLoginSuccess;
    }

    public void setImLoginSuccess(boolean imLoginSuccess) {
        this.imLoginSuccess = imLoginSuccess;
    }

    public boolean isAvStart() {
        return isAvStart;
    }

    public void setAvStart(boolean avStart) {
        isAvStart = avStart;
    }
}
