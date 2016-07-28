package com.fanwe.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fanwe.base.CallbackView;
import com.fanwe.base.CommonHelper;
import com.fanwe.base.Result;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.customview.SDSendValidateButton;
import com.fanwe.library.customview.SDSendValidateButton.SDSendValidateButtonListener;
import com.fanwe.library.utils.SDToast;
import com.fanwe.model.Check_MobActModel;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.utils.Contance;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;

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
	private SDSendValidateButton mBtnSendCode;

	@ViewInject(R.id.btn_login)
	private Button mBtnLogin;
	
	private String mStrCode;

	protected Check_MobActModel mActModel;

	private String mNumberPhone;

	CommonHelper mFragmentHelper;

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
		getIntentData();

		registeClick();
		initSDSendValidateButton();
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
			public void onSuccessListResponse(List<Result> resultList) {

			}
			@Override
			public void onSuccessResponse(String responseBody) {
				requestCaptcha();

			}
			@Override
			public void onErrorResponse(String message, String errorCode) {
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
		//开始倒计时。
		mBtnSendCode.setmDisableTime(Contance.SEND_CODE_TIME);
		mBtnSendCode.startTickWork();

		mFragmentHelper.doGetCaptcha(mNumberPhone, 0, new MgCallback() {
			@Override
			public void onSuccessListResponse(List<Result> resultList) {
				SDToast.showToast("验证码发送成功");
			}
			@Override
			public void onErrorResponse(String message, String errorCode) {
				SDToast.showToast("验证码发送失败，请重新发送");
				mBtnSendCode.setText("重新发送验证码");
				mBtnSendCode.stopTickWork();

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

	/**
	 * 初始化发送验证码按钮
	 */
	private void initSDSendValidateButton()
	{
		mBtnSendCode.setmListener(new SDSendValidateButtonListener()
		{
			@Override
			public void onTick()
			{
				
			}
			@Override
			public void onClickSendValidateButton()
			{
				checkMobileExist();

			}
		});
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

		TreeMap<String, String> params = new TreeMap<String,String>();
		params.put("mobile", mNumberPhone);
		params.put("captcha", UserConstants.USER_QUICK_LOGIN);
		OkHttpUtils.getInstance().post(null,params,new MgCallback(){

			@Override
			public void onSuccessListResponse(List<Result> resultList) {
				SDToast.showToast("登录成功");
				if(resultList!=null && resultList.size()>0){
				//解析登录数据。

				}
			}

			@Override
			public void onErrorResponse(String message, String errorCode) {
				SDToast.showToast(message);
			}
		});

	}



	protected void dealLoginNormalSuccess(User_infoModel actModel, boolean postEvent)
	{
		LocalUserModel.dealLoginSuccess(actModel, postEvent);
		getActivity().finish();
	}

	@Override
	public void onDestroy()
	{
		if(mBtnSendCode != null)
		{
			mBtnSendCode.stopTickWork();
		}
		super.onDestroy();
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);

	}


	@Override
	public void onSuccess(List<Result> responseBody) {

			SDToast.showToast("验证码发送成功");

	}

	@Override
	public void onSuccess(String responseBody) {

	}

	@Override
	public void onFailue(String responseBody) {

	}
}