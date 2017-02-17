package com.fanwe.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.fanwe.common.ImageLoaderManager;
import com.fanwe.constant.ServerUrl;
import com.fanwe.dao.LocalUserModelDao;
import com.fanwe.dao.SettingModelDao;
import com.fanwe.library.SDLibrary;
import com.fanwe.library.command.SDCommandManager;
import com.fanwe.library.config.SDLibraryConfig;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.RuntimeConfigModel;
import com.fanwe.model.SettingModel;
import com.fanwe.network.okhttp3.NetConfig;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getBusinessCircleList.ModelBusinessCircleList;
import com.fanwe.seller.model.getClassifyList.ModelClassifyList;
import com.fanwe.seller.model.getShopList.ModelShopListNavs;
import com.fanwe.shoppingcart.model.LocalShoppingcartDao;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.user.model.UserInfoNew;
import com.miguo.app.HiHomeActivity;
import com.miguo.crash.CrashHandler;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;
import com.sunday.eventbus.SDEventObserver;
import com.ta.util.netstate.TANetChangeObserver;
import com.ta.util.netstate.TANetWorkUtil.netType;
import com.ta.util.netstate.TANetworkStateReceiver;
import com.tencent.qcloud.suixinbo.avcontrollers.QavsdkControl;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.presenters.InitBusinessHelper;
import com.tencent.qcloud.suixinbo.utils.Constants;
import com.tencent.rtmp.ITXLiveBaseListener;
import com.tencent.rtmp.TXLiveBase;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


public class App extends MultiDexApplication implements SDEventObserver, TANetChangeObserver, ITXLiveBaseListener {
    public static final String TAG = App.class.getSimpleName();
    private static App mApp = null;

    public List<Class<? extends Activity>> mListClassNotFinishWhenLoginState0 = new ArrayList<Class<? extends Activity>>();
    public RuntimeConfigModel mRuntimeConfig = new RuntimeConfigModel();
    public Intent mPushIntent;

    public LocalUserModel getmLocalUser() {
        return LocalUserModelDao.queryModel();
    }

    private LinkedList<String> liveRoomIdList;
    protected String imei;
    /**
     * 当前用户信息 全局唯一用户 2016-12-01 create by zlh
     */
    UserInfoNew currentUser;

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
     * 用户在当前直播的分享领取码
     */
    public String receiveCode;
    /**
     * 直播房间列表。
     */
    public String currentRoomId;
    /**
     * 是否展示领取码对话框
     */
    public boolean isShowCode = true;
    /**
     * 领取码对话框是否已展示
     */
    public boolean isAlreadyShowCode = false;
    /**
     * 领取码
     */
    public String code = "";
    //商圈
    public static List<ModelBusinessCircleList> modelBusinessCircleLists;
    //类别
    public static List<ModelClassifyList> modelClassifyLists;
    //排序
    public static List<ModelShopListNavs> navs;

    /**
     * 当前系统时间。
     */
    public static Long sysTime = 0l;


    public void setmLocalUser(LocalUserModel localUser) {
        if (localUser != null) {
            LocalUserModelDao.insertModel(localUser);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        TXLiveBase.getInstance().listener = this;
        init();
        //当且仅当当前的协议 是HTTPS的时候才初始化证书。
        if(ServerUrl.HTTPS) {
            addSSLCert();
        }
    }

    /**
     * 将证书读入，以后应当分辨一下不同的环境 加载不同的证书。
     */
    private void addSSLCert() {
        // 添加https证书
        try {

            InputStream is = getAssets().open("certificate.cer");
            NetConfig.addCertificate(is); // 这里将证书读取出来，，放在配置中byte[]里


        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void init() {
        mApp = this;
        //crash日志捕获
        if (!ServerUrl.DEBUG){
            CrashHandler.getInstance().initCrashHandler(getApplicationContext());
        }
        ImageLoaderManager.initImageLoader();
        initSDLibrary();

//		initAppCrashHandler();
        //关闭友盟分析默认的统计方式
        MobclickAgent.openActivityDurationTrack(true);
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

        currentUser = new UserInfoNew();

        ActivityLifeManager.getInstance().init(this);
    }


    private void initJPush() {
        JPushInterface.init(this);
        JPushInterface.setDebugMode(ServerUrl.DEBUG);

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
        mListClassNotFinishWhenLoginState0.add(HiHomeActivity.class);
    }

    private void initSettingModel() {
        // 插入成功或者数据库已经存在记录
        SettingModelDao.insertOrCreateModel(new SettingModel());
        mRuntimeConfig.updateIsCanLoadImage();
        mRuntimeConfig.updateIsCanPushMessage();
    }

    public static App getApplication() {
        return mApp;
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
        if (TextUtils.isEmpty(imei)) {
            TelephonyManager telephonyManager = (TelephonyManager) this
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
            App.getInstance().setImei(telephonyManager.getDeviceId());
        }

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

    public String getToken() {
        return getCurrentUser() == null ? "" : getCurrentUser().getToken();
    }

    public void setToken(String token) {
        getCurrentUser().setToken(token);
    }

    public UserInfoNew getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserInfoNew currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * 初始化AVSDK
     */
    public void startAVSDK() {
        String userid = MySelfInfo.getInstance().getId();
        String userSign = MySelfInfo.getInstance().getUserSig();

        int appId = Constants.SDK_APPID;
        int ccType = Constants.ACCOUNT_TYPE;
        if (ServerUrl.DEBUG) {
            appId = Constants.SDK_APPID_TEST;
            ccType = Constants.ACCOUNT_TYPE_Test;
        }
        QavsdkControl.getInstance().setAvConfig(appId, ccType + "", userid, userSign);
        QavsdkControl.getInstance().startContext();

    }

    public String getUserSign() {
        return null != getCurrentUser() ? null != getCurrentUser().getUseSign() ? getCurrentUser().getUseSign() : "" : "";
    }

    public void setUserSign(String useSign) {
        if (TextUtils.isEmpty(useSign)) {
            return;
        }
        if (null != getCurrentUser()) {
            getCurrentUser().setUseSign(useSign);
        }
    }

    public String getUserNickName() {
        String nickName = "";
        if (getCurrentUser() != null) {
            nickName = getCurrentUser().getNick();
            if (TextUtils.isEmpty(nickName) || "null".equals(nickName.trim())) {
                nickName = getCurrentUser().getUser_name();
            }
            if (TextUtils.isEmpty(nickName) || "null".equals(nickName.trim())) {
                nickName = getCurrentUser().getMobile();
            }
        }
        return nickName;

    }

    public void setUserNickName(String nickName) {
        if (getCurrentUser() != null) {
            getCurrentUser().setNick(nickName);
        }
    }


    public String getUserIcon() {
        return null != getCurrentUser() ? null != getCurrentUser().getIcon() ? getCurrentUser().getIcon() : "" : "";
    }

    public void setUserIcon(String icon) {
        if (getCurrentUser() != null) {
            getCurrentUser().setIcon(icon);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void clearAllData() {
        this.imLoginSuccess = false;
        this.isAvStart = false;
        this.currentUser = new UserInfoNew();

    }

    public List<ShoppingCartInfo> getLocalShoppingCartInfo() {
        return LocalShoppingcartDao.queryModel();
    }

    public void setLocalShoppingCartInfo(List<ShoppingCartInfo> infoList) {
        if (infoList != null) {
            LocalShoppingcartDao.insertModel(infoList);
        }
    }

    public void deleteAllShoppingCartInfo() {
        LocalShoppingcartDao.deleteAllModel();
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

    public String getReceiveCode() {
        return receiveCode;
    }

    public void setReceiveCode(String receiveCode) {
        this.receiveCode = receiveCode;
    }

    public String getCurrentRoomId() {
        return currentRoomId;
    }

    public String getLastRoomId() {
        if (liveRoomIdList == null || liveRoomIdList.size() < 1) {
            return "";
        } else {
            return liveRoomIdList.getLast();
        }
    }

    public void removeLastRoomId(String currentRoomId) {
        liveRoomIdList.remove(currentRoomId);
    }

    public void addLiveRoomIdList(String currentRoomId) {
        if (TextUtils.isEmpty(currentRoomId)) {
            return;
        }
        if (liveRoomIdList == null) {
            liveRoomIdList = new LinkedList<>();
        }
        boolean needAdd = true;
        for (int i = 0; i < liveRoomIdList.size(); i++) {
            String value = liveRoomIdList.get(i);
            if (value.equals(currentRoomId)) {
                needAdd = false;
                break;

            }
        }
        if (needAdd) {
            liveRoomIdList.add(currentRoomId);
        }
    }

    public void setCurrentRoomId(String newRoomId) {
        Log.e("App setCurrentRoomId", newRoomId + "newRoomId ");
        if (!TextUtils.isEmpty(currentRoomId) && !currentRoomId.equals(newRoomId)) {
//            liveHelper.quiteAVRoom();
            App.getInstance().setAvStart(false);
            Log.e("App setCurrentRoomId", currentRoomId + "oldRoomId quiteAVRoom");
        }
        this.currentRoomId = newRoomId;
    }


    public static Long getSysTime() {
        return sysTime;
    }

    public static void setSysTime(Long sysTime) {
        App.sysTime = sysTime;
    }

    @Override
    public void OnLog(int i, String s, String s1) {

    }


}
