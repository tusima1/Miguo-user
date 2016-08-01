package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.fanwe.app.App;
import com.fanwe.app.AppConfig;
import com.fanwe.app.AppHelper;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.HomeFragment;
import com.fanwe.fragment.MarketFragment;
import com.fanwe.fragment.MyFragment;
import com.fanwe.fragment.StoreListContainerFragment;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.jpush.MessageHelper;
import com.fanwe.library.customview.SDTabItemBottom;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.customview.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.model.LocalUserModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.service.AppUpgradeService;
import com.fanwe.umeng.UmengEventStatistics;
import com.fanwe.user.presents.LoginHelper;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.LiveStartActivity;
import com.sunday.eventbus.SDBaseEvent;

@SuppressWarnings("deprecation")
public class MainActivity extends BaseActivity {

	@SuppressWarnings("deprecation")
	@ViewInject(R.id.act_main_tab_0)
	private SDTabItemBottom mTab0 = null;

	@SuppressWarnings("deprecation")
	@ViewInject(R.id.act_main_tab_1)
	private SDTabItemBottom mTab1 = null;


	@SuppressWarnings("deprecation")
	@ViewInject(R.id.act_main_tab_2)
	private SDTabItemBottom mTab2 = null;


	@SuppressWarnings("deprecation")
	@ViewInject(R.id.act_main_tab_3)
	private SDTabItemBottom mTab3 = null;

	@SuppressWarnings("deprecation")
	@ViewInject(R.id.act_main_tab_4)
	private SDTabItemBottom mTab4 = null;

	private SDViewNavigatorManager mViewManager = new SDViewNavigatorManager();

	private MyFragment mFragMyAccount;

	private long mExitTime = 0;
	private int preTab = 0;// 上次点击的tab标签页
	private int preHomeCityID = 0;//记录首页cityid-->0为异常
	private LoginHelper mLoginHelper;
	private String token;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		mLoginHelper = new LoginHelper(MainActivity.this);
		token =App.getInstance().getmUserCurrentInfo().getToken();
		init();

	}

	private void init() {
		startUpgradeService();
		initBottom();
		JpushHelper.initJPushConfig();
		MessageHelper.updateMessageCount();
		initOthers();
		initUserInfo();
	}

	//初始化用户信息。
	public void initUserInfo(){
		LocalUserModel userModel =  AppHelper.getLocalUser();
		//当前还未登录，并且用户存储中的用户信息不为空。
		if(token!=null&&userModel!=null){
			String userid = userModel.getUser_mobile();
			String password = userModel.getUser_pwd();
			mLoginHelper.doLogin(userid,password,0);
		}
	}
	private void initOthers() {
		// 初始化上次的cityID
		preHomeCityID = AppRuntimeWorker.getCity_id();
		umengTag = "首页";
	}

	private void startUpgradeService() {
		startService(new Intent(MainActivity.this, AppUpgradeService.class));
	}

	private void initBottom() {
		mTab0.setBackgroundTextTitleNumber(R.drawable.bg_number);
		mTab1.setBackgroundTextTitleNumber(R.drawable.bg_number);
		mTab2.setBackgroundTextTitleNumber(R.drawable.bg_number);
		mTab3.setBackgroundTextTitleNumber(R.drawable.bg_number);
		mTab4.setBackgroundTextTitleNumber(R.drawable.bg_number);

		mTab0.setTextTitle(SDResourcesUtil.getString(R.string.home));
		mTab1.setTextTitle(SDResourcesUtil.getString(R.string.supplier));
		mTab2.setTextTitle("直播");
		mTab3.setTextTitle(SDResourcesUtil.getString(R.string.market));
		mTab4.setTextTitle(SDResourcesUtil.getString(R.string.mine));

		mTab0.getmAttr().setmImageNormalResId(R.drawable.tab_home_normal);
		mTab1.getmAttr().setmImageNormalResId(R.drawable.tab_seller_normal);
		mTab2.getmAttr().setmImageNormalResId(R.drawable.tab_market_normal);
		mTab3.getmAttr().setmImageNormalResId(R.drawable.tab_market_normal);
		mTab4.getmAttr().setmImageNormalResId(R.drawable.tab_my_normal);

		mTab0.getmAttr().setmImageSelectedResId(R.drawable.tab_home_pressed);
		mTab1.getmAttr().setmImageSelectedResId(R.drawable.tab_seller_pressed);
		mTab2.getmAttr().setmImageSelectedResId(R.drawable.tab_market_pressed);
		mTab3.getmAttr().setmImageSelectedResId(R.drawable.tab_market_pressed);
		mTab4.getmAttr().setmImageSelectedResId(R.drawable.tab_my_pressed);


		mTab0.getmAttr().setmTextColorNormalResId(R.color.text_home_menu_normal);
		mTab1.getmAttr().setmTextColorNormalResId(R.color.text_home_menu_normal);
		mTab2.getmAttr().setmTextColorNormalResId(R.color.text_home_menu_normal);
		mTab3.getmAttr().setmTextColorNormalResId(R.color.text_home_menu_normal);
		mTab4.getmAttr().setmTextColorNormalResId(R.color.text_home_menu_normal);

		mTab0.getmAttr().setmTextColorSelectedResId(R.color.text_home_menu_selected);
		mTab1.getmAttr().setmTextColorSelectedResId(R.color.text_home_menu_selected);
		mTab2.getmAttr().setmTextColorSelectedResId(R.color.text_home_menu_selected);
		mTab3.getmAttr().setmTextColorSelectedResId(R.color.text_home_menu_selected);
		mTab4.getmAttr().setmTextColorSelectedResId(R.color.text_home_menu_selected);

		SDViewBase[] items = new SDViewBase[]{mTab0, mTab1, mTab2, mTab3, mTab4};

		mViewManager.setItems(items);
		mViewManager.setmListener(new SDViewNavigatorManagerListener() {
			@Override
			public void onItemClick(View v, int index) {
				switch (index) {
					case 0:
						click0();
						break;
					case 1:
						click1();
						break;
					case 2:
						click2();
						break;
					case 3:
						click3();
						break;
					case 4:
						click4();
					default:
						break;
				}
				preTab = index;
			}
		});
		// 如果要加入guideActivity请注释这行代码,并打开initGuideResult();方法...=.=
		mViewManager.setSelectIndex(0, mTab0, true);

		mTab0.setTitleGone();
		mTab1.setTitleGone();
		mTab2.setTitleGone();
		mTab3.setTitleGone();
		mTab4.setTitleGone();
	}

	public void setSelectIndex(int index, View view, boolean notify) {
		if (mViewManager != null) {
			mViewManager.setSelectIndex(index, view, notify);
		}
	}

	/**
	 * 首页
	 */
	protected void click0() {
		UmengEventStatistics.sendEvent(this, UmengEventStatistics.MAIN_1);
		getSDFragmentManager().toggle(R.id.act_main_fl_content, null, HomeFragment.class);
		refreshHomeFragment();
	}

	/**
	 * 商家
	 */
	protected void click1() {
		UmengEventStatistics.sendEvent(this, UmengEventStatistics.MAIN_2);
		getSDFragmentManager().toggle(R.id.act_main_fl_content, null, StoreListContainerFragment.class);

	}

	/**
	 * 直播
	 */
	protected void click2() {

		if (!AppHelper.isLogin(this)) // 未登录
		{
			mViewManager.setSelectIndexLast(false);
		} else {
//            getSDFragmentManager().toggle(R.id.act_main_fl_content, null, MyDistributionFragment.class);
			startActivity(new Intent(this, LiveStartActivity.class));
		}
	}

	/**
	 * 市场
	 */
	protected void click3() {
		UmengEventStatistics.sendEvent(this, UmengEventStatistics.MAIN_3);
		getSDFragmentManager().toggle(R.id.act_main_fl_content, null, MarketFragment.class);
		if (preTab == 0 || preTab == 2 || preTab == 3) {
			if (((MarketFragment) getSDFragmentManager().getmFragmentLastToggle()).mPtrlv_content != null) {
				((MarketFragment) getSDFragmentManager().getmFragmentLastToggle()).mPtrlv_content.setRefreshing();
			}
		}
	}

	/**
	 * 我的账户
	 */
	protected void click4() {
		UmengEventStatistics.sendEvent(this, UmengEventStatistics.MAIN_4);
		if (token==null)  // 未登录
		{
			startActivity(new Intent(this, LoginActivity.class));
		} else {
			mFragMyAccount = (MyFragment) getSDFragmentManager().toggle(R.id.act_main_fl_content, null,
					MyFragment.class);
		}
	}

	@Override
	public void onEventMainThread(SDBaseEvent event) {
		super.onEventMainThread(event);
		switch (EnumEventTag.valueOf(event.getTagInt())) {
			case LOGIN_SUCCESS:
				mViewManager.setSelectIndex(preTab, mTab0, true);
				break;
			case LOGOUT:
				mTab3.setTextTitleNumber(null);
				removeMyAccountFragment();
				mViewManager.setSelectIndex(0, mTab0, true);
				break;
			case TEMP_LOGIN:
				removeMyAccountFragment();
				mViewManager.setSelectIndex(0, mTab0, true);
				break;
			case UN_LOGIN:
				removeMyAccountFragment();
				mViewManager.setSelectIndex(0, mTab0, true);
				break;
			default:
				break;
		}
	}

	private void removeMyAccountFragment() {
		getSDFragmentManager().remove(mFragMyAccount);
		mFragMyAccount = null;
	}

	@Override
	public void onBackPressed() {
		exitApp();
	}

	private void exitApp() {
		if (System.currentTimeMillis() - mExitTime > 2000) {
			SDToast.showToast("再按一次退出!");
		} else {
			App.getApplication().exitApp(true);
		}
		mExitTime = System.currentTimeMillis();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			if (resultCode == MyCaptureActivity.RESULT_CODE_SCAN_SUCCESS) {
				String result = data.getStringExtra("extra_result_success_string");
				if (result.substring(0, 6).equals("https:") || result.substring(0, 6).equals("http:/")) {
					if (result.contains("offline")) {
						Intent intentStore = new Intent(this, StoreConfirmOrderActivity.class);
						intentStore.putExtra("mID",
								Integer.parseInt(result.split("\\/")[result.split("\\/").length - 1]));
						startActivity(intentStore);

					} else if (result.contains(".html#") && !result.contains("offline")) {
						String session = AppConfig.getSessionId();
						String url = result.replace(".html#", ".html?from=app&sess_id=" + session + "#");
						Intent intentWeb = new Intent(this, CaptureResultWebActivity.class);
						intentWeb.putExtra("url", url);
						startActivity(intentWeb);
					} else if (result.contains(".html#") && !result.contains("offline")) {
						String url = result.replace(".html#", ".html?from=app#");
						Intent intentWeb = new Intent(this, CaptureResultWebActivity.class);
						intentWeb.putExtra("url", url);
						startActivity(intentWeb);

					} else if (result.contains(".html#") && !result.contains("offline")) {
						String url = result.replace(".html#", ".html?from=app#");
						Intent intentWeb = new Intent(this, CaptureResultWebActivity.class);
						intentWeb.putExtra("url", url);
						startActivity(intentWeb);
					} else if (result.contains("/#") && !result.contains("offline")) {
						String session = AppConfig.getSessionId();
						String url = result.replace("/#", "/?from=app&sess_id=" + session + "#");
						Intent intentWeb = new Intent(this, CaptureResultWebActivity.class);
						intentWeb.putExtra("url", url);
						startActivity(intentWeb);
					}
				} else {
					SDToast.showToast("无法识别");
				}
			}
		} else if (resultCode == MarketFragment.CITY_RESULT) {
			Bundle cityData = data.getExtras();
			if (cityData == null) {
				SDToast.showToast("位置信息获取失败!");
				return;
			}
			String cityName = cityData.getString("city", "");
			int cityID = cityData.getInt("cityID", -1);
			preHomeCityID = -1;
			if (!TextUtils.isEmpty(cityName) && cityID != -1) {
				// 通知城市的name和id获取正常
				Fragment lastToggle = getSDFragmentManager().getmFragmentLastToggle();
				if (lastToggle instanceof MarketFragment) {
					((MarketFragment) lastToggle).setCityIdTitle(cityName, cityID);
				}
			}
		} else if (resultCode == 8888) {
			//从首页的城市选择Activity回来
			Fragment lastToggle = getSDFragmentManager().getmFragmentLastToggle();
			if (lastToggle instanceof HomeFragment) {
				((HomeFragment) lastToggle).refreshData();
				preHomeCityID = AppRuntimeWorker.getCity_id();
			}
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (!AppHelper.isLogin()) {
			// 如果未登录,跳转到首页
			if (preTab == 2 || preTab == 4) {
				mViewManager.setSelectIndex(0, mTab0, true);
			}
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// onNewIntent在onRestart 前调用.
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			int index = bundle.getInt("index", 0);
			switch (index) {
				case 0:
					mViewManager.setSelectIndex(index, mTab0, true);
					break;
				case 1:
					mViewManager.setSelectIndex(index, mTab1, true);
					break;
				case 2:
					mViewManager.setSelectIndex(index, mTab2, true);
					break;
				case 3:
					mViewManager.setSelectIndex(index, mTab3, true);
					break;
				case 4:
					mViewManager.setSelectIndex(index, mTab4, true);
					mFragMyAccount.refreshFragment();
					break;
				default:
					break;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			int index = bundle.getInt("index", 0);
			switch (index) {
				case 0:
					mViewManager.setSelectIndex(index, mTab0, true);
					break;
				case 1:
					mViewManager.setSelectIndex(index, mTab1, true);
					break;
				case 2:
					mViewManager.setSelectIndex(index, mTab2, true);
					break;
				case 3:
					mViewManager.setSelectIndex(index, mTab3, true);
					break;
				case 4:
					mViewManager.setSelectIndex(index, mTab4, true);
					mFragMyAccount.refreshFragment();
					break;
				default:
					break;
			}
		}
		refreshHomeFragment();
	}

	private void refreshHomeFragment() {
		/** 城市变动的自动刷新 **/
		int city_id = AppRuntimeWorker.getCity_id();
		Fragment isHomeFragment = getSDFragmentManager().getmFragmentLastToggle();
		if (preHomeCityID != 0 && preHomeCityID != city_id && isHomeFragment instanceof HomeFragment) {
			((HomeFragment) isHomeFragment).refreshData();
			preHomeCityID = city_id;
		}
	}
}
