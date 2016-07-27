package com.fanwe.app;

import java.util.ArrayList;
import java.util.List;

import com.fanwe.BaseActivity;
import com.fanwe.MainActivity;
import com.fanwe.baidumap.BaiduMapManager;
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
import com.fanwe.utils.CrashHandler;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;
import com.sunday.eventbus.SDEventObserver;
import com.ta.util.netstate.TANetChangeObserver;
import com.ta.util.netstate.TANetWorkUtil.netType;
import com.ta.util.netstate.TANetworkStateReceiver;
import com.umeng.analytics.MobclickAgent;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import cn.jpush.android.api.JPushInterface;


public class App extends Application implements SDEventObserver, TANetChangeObserver
{

	private static App mApp = null;

	public List<Class<? extends BaseActivity>> mListClassNotFinishWhenLoginState0 = new ArrayList<Class<? extends BaseActivity>>();
	public RuntimeConfigModel mRuntimeConfig = new RuntimeConfigModel();
	public Intent mPushIntent;

	public LocalUserModel getmLocalUser()
	{
		return LocalUserModelDao.queryModel();
	}

	protected String imei;

	/**
	 * 自我引用 .
	 */
	private static App myApplication;
	public void setmLocalUser(LocalUserModel localUser)
	{
		if (localUser != null)
		{
			LocalUserModelDao.insertModel(localUser);
		}
	}


	@Override
	public void onCreate()
	{
		super.onCreate();
		myApplication = this;

		init();
	}

	private void init()
	{
		mApp = this;
		ImageLoaderManager.initImageLoader();
		initSDLibrary();
		
//		initAppCrashHandler();
		//关闭友盟分析默认的统计方式
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.setCatchUncaughtExceptions(true);
		
		
		
		initBaiduMap();
		//初始化友盟分享
		UmengShareManager.initConfig();
	
		SDEventManager.register(this);
		TANetworkStateReceiver.registerObserver(this);
		initSettingModel();
		initJPush();
		addClassesNotFinishWhenLoginState0();
		SDCommandManager.getInstance().initialize();
		LogUtil.isDebug = ServerUrl.DEBUG;

		initDeviceId();
	}

	
	private void initJPush() {
		JPushInterface.init(this);
		JpushHelper.registerAll();
	}

	private void initSDLibrary()
	{
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

	private void addClassesNotFinishWhenLoginState0()
	{
		mListClassNotFinishWhenLoginState0.add(MainActivity.class);
	}

	private void initAppCrashHandler()
	{
		if (!ServerUrl.DEBUG)
		{
			CrashHandler crashHandler = CrashHandler.getInstance();
			crashHandler.init(getApplicationContext());
		}
	}

	private void initSettingModel()
	{
		// 插入成功或者数据库已经存在记录
		SettingModelDao.insertOrCreateModel(new SettingModel());
		mRuntimeConfig.updateIsCanLoadImage();
		mRuntimeConfig.updateIsCanPushMessage();
	}

	private void initBaiduMap()
	{
		BaiduMapManager.getInstance().init(this);
	}

	public static App getApplication()
	{
		return mApp;
	}

	public void exitApp(boolean isBackground)
	{
		AppConfig.setRefId("");
		SDActivityManager.getInstance().finishAllActivity();
		SDEventManager.post(EnumEventTag.EXIT_APP.ordinal());
		if (isBackground)
		{

		} else
		{
			System.exit(0);
		}
	}

	public void clearAppsLocalUserModel()
	{
		
		AppConfig.setSessionId("");
		AppConfig.setRefId("");
		LocalUserModelDao.deleteAllModel();
		
	}

	public static String getStringById(int resId)
	{
		return getApplication().getString(resId);
	}

	@Override
	public void onConnect(netType type)
	{
		mRuntimeConfig.updateIsCanLoadImage();
	}

	@Override
	public void onDisConnect()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onTerminate()
	{
		SDEventManager.unregister(this);
		super.onTerminate();
	}

	@Override
	public void onEvent(SDBaseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onEventBackgroundThread(SDBaseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onEventAsync(SDBaseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		// TODO Auto-generated method stub

	}

	public void initDeviceId(){
		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
	}

	public String getImei() {
		return imei;
	}
	/**
	 * 返回单实例.
	 *
	 * @return HsApplication
	 */
	public static App getInstance() {
		return myApplication;
	}
}
