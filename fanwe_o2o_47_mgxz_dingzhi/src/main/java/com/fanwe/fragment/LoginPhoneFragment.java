package com.fanwe.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.base.CommonHelper;
import com.fanwe.base.Result;
import com.fanwe.base.Root;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.customview.SDSendValidateButton;
import com.fanwe.library.customview.SDSendValidateButton.SDSendValidateButtonListener;
import com.fanwe.library.utils.MD5Util;
import com.fanwe.library.utils.SDToast;
import com.fanwe.model.Check_MobActModel;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.UserInfoNew;
import com.fanwe.user.presents.LoginHelper;
import com.fanwe.utils.Contance;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;

import java.lang.reflect.Type;
import java.util.List;
import java.util.TreeMap;

public class LoginPhoneFragment extends LoginBaseFragment implements CallbackView
{

	public static final String TAG = LoginPhoneFragment.class.getSimpleName();
	public static final String EXTRA_PHONE_NUMBER = "extra_phone_number";

	@ViewInject(R.id.et_mobile)
	private ClearEditText mEtMobile;

	@ViewInject(R.id.et_code)
	private ClearEditText mEtCode;

	@ViewInject(R.id.btn_send_code)
	private Button mBtnSendCode;

	@ViewInject(R.id.btn_login)
	private Button mBtnLogin;
	
	private String mStrCode;

	protected Check_MobActModel mActModel;

	private String mNumberPhone;

	CommonHelper mFragmentHelper;
	private TimeCount time;
	LoginHelper mLoginHelper;

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_phone_login);
	}

	@Override
	protected void init()
	{
		super.init();
		mFragmentHelper = new CommonHelper(getActivity(),this);
		mLoginHelper = new LoginHelper(getActivity(),getActivity(),null);
		getIntentData();

		registeClick();
		mBtnSendCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			   checkMobileExist();
			}
		});
		time = new TimeCount(60000, 1000);//构造CountDownTimer对象
	}

	/**
	 *  判断手机号是否存在。
     */
	public void checkMobileExist() {
		mNumberPhone = mEtMobile.getText().toString();
		if (TextUtils.isEmpty(mNumberPhone)) {
			SDToast.showToast("请输入手机号码");
			return;
		}

		mFragmentHelper.doCheckMobileExist(mNumberPhone, new MgCallback() {

			@Override
			public void onSuccessResponse(String responseBody) {
				time.start();//开始计时
				requestCaptcha();

			}
			@Override
			public void onErrorResponse(String message, String errorCode)
			{
				SDToast.showToast(message);
			}
		});
	}

	/**
	 *手机验证码发送。
	 */
	public void requestCaptcha(){
	   mNumberPhone = mEtMobile.getText().toString();

		if (TextUtils.isEmpty(mNumberPhone))
		{
			SDToast.showToast("请输入手机号码");
			return;
		}


		mFragmentHelper.doGetCaptcha(mNumberPhone, 4, new MgCallback() {


			@Override
			public void onErrorResponse(String message, String errorCode) {
				SDToast.showToast("验证码发送失败，请重新发送");
				mBtnSendCode.setText("重新发送验证码");
				time.onFinish();

			}

			@Override
			public void onSuccessResponse(String responseBody) {
				SDToast.showToast("验证码发送成功");
			}
		});
		}

	protected void getIntentData()
	{
		String mobile = getActivity().getIntent().getStringExtra(EXTRA_PHONE_NUMBER);
		
		if (!TextUtils.isEmpty(mobile))
		{
			mEtMobile.setText(mobile);
		}
	}


	private void registeClick()
	{
		mBtnLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.btn_login:
			quickLogin();
			break;

		default:
			break;
		}
	}
	/**
	 *快捷 登录 接口。
	 */
	private void quickLogin() {
		mNumberPhone = mEtMobile.getText().toString();
		if (TextUtils.isEmpty(mNumberPhone))
		{
			SDToast.showToast("请输入手机号码!");
			return;
		}
		mStrCode = mEtCode.getText().toString();
		if (TextUtils.isEmpty(mStrCode))
		{
			SDToast.showToast("请输入验证码!");
			return;
		}
		mLoginHelper.doQuickLogin(mNumberPhone,mStrCode);


	}


	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);

	}


	@Override
	public void onSuccess(String responseBody) {
		SDToast.showToast("验证码发送成功");
	}

	@Override
	public void onSuccess(String method, List datas) {

	}

	@Override
	public void onFailue(String responseBody) {

	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {//计时完毕时触发
			mBtnSendCode.setText("重新验证");
			mBtnSendCode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {//计时过程显示
			mBtnSendCode.setClickable(false);
			mBtnSendCode.setText(millisUntilFinished / 1000 + "秒");
		}

	}
}