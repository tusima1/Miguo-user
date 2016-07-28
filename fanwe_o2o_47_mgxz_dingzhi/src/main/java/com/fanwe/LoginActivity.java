package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.base.CallbackView;
import com.fanwe.base.Result;
import com.fanwe.constant.Constant.EnumLoginState;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.LoginFragment;
import com.fanwe.fragment.LoginPhoneFragment;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.library.customview.SDTabItemCorner;
import com.fanwe.library.customview.SDTabItemCorner.EnumTabPosition;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.customview.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_login);
		init();
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
				Intent intent = new Intent(mActivity, ModifyPasswordActivity.class);
				startActivity(intent);
			}
		});
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
		startRegisterActivity();
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

	protected void startRegisterActivity()
	{
		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		startActivity(intent);
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSuccess(List<Result> responseBody) {

	}

	@Override
	public void onSuccess(String responseBody) {

	}

	@Override
	public void onFailue(String responseBody) {

	}
}