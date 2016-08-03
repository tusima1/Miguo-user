package com.fanwe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.base.Root;
import com.fanwe.constant.Constant.EnumLoginState;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.LoginFragment;
import com.fanwe.fragment.LoginPhoneFragment;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.customview.SDTabItemCorner;
import com.fanwe.library.customview.SDTabItemCorner.EnumTabPosition;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.customview.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.UserInfoNew;
import com.fanwe.work.AppRuntimeWorker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import simbest.com.sharelib.ILoginCallback;
import simbest.com.sharelib.ShareUtils;

public class LoginActivity extends BaseActivity implements CallbackView
{

	public static final String EXTRA_SELECT_TAG_INDEX = "extra_select_tag_index";

	@ViewInject(R.id.ll_tabs)
	private LinearLayout mLl_tabs = null;

	@ViewInject(R.id.tv_find_password)
	private TextView mTv_find_password = null;

	@ViewInject(R.id.act_login_new_tab_login_normal)
	private SDTabItemCorner mTabLoginNormal = null;

	@ViewInject(R.id.act_login_new_tab_login_phone)
	private SDTabItemCorner mTabLoginPhone = null;

	private SDViewNavigatorManager mViewManager = new SDViewNavigatorManager();

	private int mSelectTabIndex = 0;

	private List<Integer> mListSelectIndex = new ArrayList<Integer>();

	/**
	 * qqq登录。
	 */
	@ViewInject(R.id.qq_login)
	private Button qq_login;
	/**
	 * 微博登录
	 */

	@ViewInject(R.id.weibo_login)
	private Button weibo_login;
	/**
	 * 微信登录
	 */

	@ViewInject(R.id.weixin_login)
	private Button weixin_login;
	@ViewInject(R.id.testViews)
  private EditText testViews;
   private String openId;

	//1:qq，2:微信，3：微博
	String platformType="";
	private ShareUtils su;
	SHARE_MEDIA platform = null;
	/**
	 * 头像。
	 */
	String icon="";
	/**
	 * 密码。
	 */
	String nick="";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_login);
		init();
		//友盟授权登录初始化。
		su = new ShareUtils(this);
	}

	private void init()
	{

		getIntentData();
		initTitle();
		changeViewState();
		registerClick();

	}

	private void registerClick()
	{
		mTv_find_password.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(LoginActivity.this, ModifyPasswordActivity.class);
				startActivity(intent);

			}
		});

		qq_login.setOnClickListener(this);
		weibo_login.setOnClickListener(this);
		weixin_login.setOnClickListener(this);
	}

	private void getIntentData()
	{
		mListSelectIndex.add(0);
		mListSelectIndex.add(1);

		mSelectTabIndex = getIntent().getIntExtra(EXTRA_SELECT_TAG_INDEX, 0);
		if (!mListSelectIndex.contains(mSelectTabIndex))
		{
			mSelectTabIndex = 0;
		}
	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop("登录");
		mTitle.initRightItem(1);
		mTitle.getItemRight(0).setTextBot("注册");
	}

	@Override
	public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
	{
		startRegisterActivity(false,"","","");
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		setIntent(intent);
		init();
		super.onNewIntent(intent);
	}

	private void changeViewState()
	{
		EnumLoginState state = AppRuntimeWorker.getLoginState();
		switch (state)
		{
		case LOGIN_EMPTY_PHONE:
			changeViewLoginEmptyPhone();
			break;
		case UN_LOGIN:
			changeViewUnLogin();
			break;
		case LOGIN_NEED_BIND_PHONE:
			changeViewUnLogin();
			showBindPhoneDialog();
			break;
		case LOGIN_NEED_VALIDATE:
			changeViewUnLogin();
			break;

		default:
			break;
		}
	}

	private void showBindPhoneDialog()
	{
		Intent intent = new Intent(getApplicationContext(), BindMobileActivity.class);
		startActivity(intent);
		finish();
	}

	private void changeViewLoginEmptyPhone()
	{
		mLl_tabs.setVisibility(View.GONE);
		clickLoginNormal();
	}

	private void changeViewUnLogin()
	{
		mTabLoginNormal.setTabName("账号登录");
		mTabLoginNormal.setTabTextSizeSp(18);
		mTabLoginNormal.setmPosition(EnumTabPosition.FIRST);

		mTabLoginPhone.setTabName("快捷登录");
		mTabLoginPhone.setTabTextSizeSp(18);
		mTabLoginPhone.setmPosition(EnumTabPosition.LAST);

		mViewManager.setItems(new SDViewBase[] { mTabLoginNormal, mTabLoginPhone });

		mViewManager.setmListener(new SDViewNavigatorManagerListener()
		{
			@Override
			public void onItemClick(View v, int index)
			{
				switch (index)
				{
				case 0: // 正常登录
					clickLoginNormal();
					break;
				case 1: // 快捷登录
					clickLoginPhone();
					break;
				default:
					break;
				}
			}
		});

		mViewManager.setSelectIndex(mSelectTabIndex, null, true);
	}
	@Override
	public void onClick(View v) {
		SHARE_MEDIA platform = null;
		switch (v.getId()) {
			case R.id.qq_login:
				platform = SHARE_MEDIA.QQ;
				goToAuth(platform);
				break;
			case R.id.weibo_login:
				platform = SHARE_MEDIA.SINA;
			//	startRegisterActivity(true,"1",icon,nick);
			goToAuth(platform);
				break;
			case R.id.weixin_login:
				platform = SHARE_MEDIA.WEIXIN;
				goToAuth(platform);
				break;

			default:
				break;
		}
	}
	public void printData(Map<String, String> data){
		StringBuffer str = new StringBuffer();
		for(Map.Entry<String, String> entry:data.entrySet()){
			str.append(entry.getKey()+"--->"+entry.getValue()+"\n");
		}
		testViews.setText(str.toString());
	}
	/**
	 * 跳转到相应的授权页。
	 *
	 * @param platform
	 */
	public void goToAuth(final SHARE_MEDIA platform) {
		su.login(platform, new ILoginCallback() {
			@Override
			public void onSuccess(Map<String, String> data) {
				printData(data);
				if(platform .equals(SHARE_MEDIA.WEIXIN)){
					platformType = "2";
					openId = data.get("openid");
					nick = data.get("nickname");
                    icon=data.get("headimgurl");
				}else if(platform .equals(SHARE_MEDIA.QQ)){
					platformType = "1";
					openId = data.get("openid");
					icon = data.get("profile_image_url");
					nick = data.get("screen_name");

				}else if(platform .equals(SHARE_MEDIA.SINA)){
					platformType = "3";
					String returnData = (String)data.get("result");
					Gson gson = new Gson();
					HashMap<String,Object> maps = gson.fromJson(returnData,HashMap.class);
					openId = maps.get("id").toString();
				}
				if(TextUtils.isEmpty(openId)){
					Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
					return;
				}
			 thirdLogin(openId,platformType,icon,nick);
			}

			@Override
			public void onFaild(String msg) {
				Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCancel() {
				Toast.makeText(LoginActivity.this, "取消登录", Toast.LENGTH_SHORT).show();
			}
		});
	}
	/**
	 * 正常登录的选项卡被选中
	 */
	protected void clickLoginNormal()
	{
		SDViewUtil.show(mTv_find_password);
		getSDFragmentManager().toggle(R.id.act_login_fl_content, null, LoginFragment.class);
	}

	/**
	 * 手机号快捷登录的选项卡被选中
	 */
	protected void clickLoginPhone()
	{
		SDViewUtil.hide(mTv_find_password);
		getSDFragmentManager().toggle(R.id.act_login_fl_content, null, LoginPhoneFragment.class);
	}

	protected void startRegisterActivity(boolean third,String type,String icon,  String nick)
	{

		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		if(third&&!TextUtils.isEmpty(openId)) {
			intent.putExtra(UserConstants.THIRD_OPENID, openId);
			intent.putExtra(UserConstants.THIRD_PLATFORM, type);
			intent.putExtra(UserConstants.THIRD_ICON, icon);
			intent.putExtra(UserConstants.THIRD_NICK, nick);

		}else{
			startActivity(intent);
			finish();
		}

	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		switch (EnumEventTag.valueOf(event.getTagInt()))
		{
		case LOGIN_SUCCESS:
			JpushHelper.registerAll();
			finish();
			break;

		default:
			break;
		}
	}



	/**
	 *
	 * @param openId
	 * @param platformType
	 * @param icon 头像地址
	 * @param icon 昵称
     */
	public void thirdLogin(String openId ,final String platformType,final String icon,  final String nick){
		if(TextUtils.isEmpty(openId)){
			return;
		}
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("openid",openId);
		params.put("platform",platformType);
		if(!TextUtils.isEmpty(icon)) {
			params.put("icon", icon);
		}
		if(!TextUtils.isEmpty(nick)) {
			try {
				params.put("nick", URLEncoder.encode(nick,"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		params.put("method",UserConstants.TRHID_LOGIN_URL);
		OkHttpUtils.getInstance().get(null, params, new MgCallback() {
			@Override
			public void onErrorResponse(String message, String errorCode) {

			}

			@Override
			public void onSuccessResponse(String responseBody) {

				Type type = new TypeToken<Root<UserInfoNew>>() {
				}.getType();
				Gson gson = new Gson();
				Root<UserInfoNew> root = gson.fromJson(responseBody, type);
				String statusCode = root.getStatusCode();
				if("210".equals(statusCode)) {
					UserInfoNew userInfoNew = (UserInfoNew) validateBody(root);
					if (userInfoNew != null) {
						if (userInfoNew != null) {
							App.getInstance().getmUserCurrentInfo().setUserInfoNew(userInfoNew);
							User_infoModel model = new User_infoModel();
							model.setUser_id(userInfoNew.getUser_id());
							model.setUser_name(userInfoNew.getUser_name());
							dealLoginSuccess(model);
						}
					}
				}else if("300".equals(statusCode)){
					startRegisterActivity(true,platformType,icon,nick);
				}else{

				}

			}
		});

	}
	protected void dealLoginSuccess(User_infoModel actModel) {
		LocalUserModel.dealLoginSuccess(actModel, true);
		Activity lastActivity = SDActivityManager.getInstance().getLastActivity();
		if (lastActivity instanceof MainActivity) {
			finish();
		} else {
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
		}
	}

		@Override
	protected void onDestroy() {
		super.onDestroy();
	}



	@Override
	public void onSuccess(String responseBody) {

	}

	@Override
	public void onSuccess(String method, List datas) {

	}

	@Override
	public void onFailue(String responseBody) {

	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		su.onActivityResult(requestCode, resultCode, data);
	}






}