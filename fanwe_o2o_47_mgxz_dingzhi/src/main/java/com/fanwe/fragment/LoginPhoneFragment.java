package com.fanwe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fanwe.SetPwActivity;
import com.fanwe.base.CallbackView;
import com.fanwe.base.CommonHelper;
import com.fanwe.base.Result;
import com.fanwe.common.CommonInterface;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.customview.SDSendValidateButton;
import com.fanwe.library.customview.SDSendValidateButton.SDSendValidateButtonListener;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.dialog.SDDialogCustom.SDDialogCustomListener;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDToast;
import com.fanwe.model.Check_MobActModel;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Sms_send_sms_codeActModel;
import com.fanwe.model.UserInfoModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.network.MgCallback;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.Contance;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;

import java.util.List;

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

	public void initRequest()
	{
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
//		model.putCtl("user");
//		model.putAct("check_mobile");
//		model.put("mobile", mNumberPhone);
//		SDRequestCallBack<Check_MobActModel> handler = new SDRequestCallBack<Check_MobActModel>()
//		{
//					@Override
//					public void onStart()
//					{
//						SDDialogManager.showProgressDialog("请稍候...");
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo)
//					{
//						mActModel = actModel;
//						if(actModel.getExists() == 1)
//						{
//							requestSendCode();
//						}else
//						{
//							showChangeLocationDialog();
//							SDViewBinder.setTextView(mBtnLogin, "提交");
//						}
//					}
//
//					@Override
//					public void onFinish()
//					{
//						SDDialogManager.dismissProgressDialog();
//					}
//				};
//				InterfaceServer.getInstance().requestInterface(model, handler);
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
				initRequest();
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
			clickLogin();
			break;

		default:
			break;
		}
	}

	/**
	 * 快捷登录
	 */
	private void clickLogin()
	{
		if(mActModel == null)
		{
			return;
		}
		mStrCode = mEtCode.getText().toString();
		if (validateParams())
		{
			requestShortcutLogin();
		}else
		{
			requestShortSet();
		}
	}

	/**
	 *快捷 登录 接口。
	 */
	private void requestShortSet() {
		
		if (TextUtils.isEmpty(mNumberPhone))
		{
			SDToast.showToast("请输入手机号码!");
			return;
		}
		
		if (TextUtils.isEmpty(mStrCode))
		{
			SDToast.showToast("请输入验证码!");
			return;
		}
		RequestModel model = new RequestModel();
		model.putCtl("user");
		model.putAct("verify_login");
		model.put("mobile", mNumberPhone);
		model.put("sms_verify", mStrCode);
		SDRequestCallBack<UserInfoModel> handler = new SDRequestCallBack<UserInfoModel>()
		{
			
			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("请稍候...");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if(actModel.getVerify_status() == 0)
				{
					SDToast.showToast("验证码不正确");
				}else
				{
					/*if(actModel.getUser() == null)
					{*/
						Intent intent = new Intent(getActivity(),SetPwActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("mobile", mNumberPhone);
						bundle.putString("sms_verify",mStrCode);
						intent.putExtras(bundle);
						startActivity(intent);
					/*}*/
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				
			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);
	}

	private boolean validateParams()
	{
		if(mActModel.getExists() != 1 || mActModel.getIs_tmp() == 1)//手机号未被注册
		{
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 验证码接口
	 * 
	 */
	private void requestSendCode()
	{

		CommonInterface.requestValidateCode(mNumberPhone, 0, new SDRequestCallBack<Sms_send_sms_codeActModel>()
				{
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo)
					{
						switch (actModel.getStatus())
						{
						case -1:
							break;
						case 1:
							mBtnSendCode.setmDisableTime(actModel.getLesstime());
							mBtnSendCode.startTickWork();
							break;
						default:
							break;
						}
					}

					@Override
					public void onStart()
					{
						SDDialogManager.showProgressDialog("请稍候...");
					}

					@Override
					public void onFinish()
					{
						SDDialogManager.dismissProgressDialog();
					}

					@Override
					public void onFailure(HttpException error, String msg)
					{
					}
				});
		
	}

	private void showChangeLocationDialog()
	{
		((SDDialogCustom) new SDDialogConfirm().setTextContent(" 此号码未被注册，获取验证码后即同意《注册协议》"+"\n").setGrativity(Gravity.CENTER_HORIZONTAL))
		.setTextColorTitle(R.color.gray).setTextConfirm("获取验证码").setmListener(new SDDialogCustomListener()
		{
			@Override
			public void onDismiss(SDDialogCustom dialog)
			{
				
			}
			@Override
			public void onClickConfirm(View v, SDDialogCustom dialog)
			{
				CommonInterface.requestValidateCode(mNumberPhone, 0, new SDRequestCallBack<Sms_send_sms_codeActModel>()
						{
							@Override
							public void onSuccess(ResponseInfo<String> responseInfo)
							{
								switch (actModel.getStatus())
								{
								case -1:
									break;
								case 1:
									mBtnSendCode.setmDisableTime(actModel.getLesstime());
									mBtnSendCode.startTickWork();
									break;
								default:
									break;
								}
							}

							@Override
							public void onStart()
							{
								SDDialogManager.showProgressDialog("请稍候...");
							}

							@Override
							public void onFinish()
							{
								SDDialogManager.dismissProgressDialog();
							}
							
							@Override
							public void onFailure(HttpException error, String msg)
							{
							}
						});
			}
			
			@Override
			public void onClickCancel(View v, SDDialogCustom dialog)
			{
			}
		}).show();
		
	}
	/**
	 * 快捷登录接口
	 */
	private void requestShortcutLogin()
	{
		if (TextUtils.isEmpty(mNumberPhone))
		{
			SDToast.showToast("请输入手机号码!");
			return;
		}
		
		if (TextUtils.isEmpty(mStrCode))
		{
			SDToast.showToast("请输入验证码!");
			return;
		}
		
		RequestModel model = new RequestModel();
		model.putCtl("user");
		model.putAct("verify_login");
		model.put("mobile", mNumberPhone);
		model.put("sms_verify", mStrCode);
		SDRequestCallBack<UserInfoModel> handler = new SDRequestCallBack<UserInfoModel>()
		{
			
			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("请稍候...");
			}
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if(actModel.getVerify_status() == 0)
				{
					SDToast.showToast("验证码不正确");
				}else
				{
					
					dealLoginNormalSuccess(actModel.getUser(), true);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				
			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);

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
		switch (EnumEventTag.valueOf(event.getTagInt()))
		{
		case CONFIRM_IMAGE_CODE:
			if (SDActivityManager.getInstance().isLastActivity(getActivity()))
			{
				requestSendCode();
			}
			break;

		default:
			break;
		}
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