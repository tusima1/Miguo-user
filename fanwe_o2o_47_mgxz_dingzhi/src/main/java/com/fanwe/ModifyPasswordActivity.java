package com.fanwe;

import com.fanwe.app.AppHelper;
import com.fanwe.common.CommonInterface;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.customview.SDSendValidateButton;
import com.fanwe.library.customview.SDSendValidateButton.SDSendValidateButtonListener;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDToast;
import com.fanwe.model.Check_MobActModel;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Sms_send_sms_codeActModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class ModifyPasswordActivity extends BaseActivity
{

	@ViewInject(R.id.svb_validate)
	private SDSendValidateButton mSvb_validate;

	@ViewInject(R.id.cet_mobile)
	private ClearEditText mCet_mobile;

	@ViewInject(R.id.cet_code)
	private ClearEditText mCet_code;

	@ViewInject(R.id.cet_pwd)
	private ClearEditText mCet_pwd;

	@ViewInject(R.id.cet_pwd_confirm)
	private ClearEditText mCet_pwd_confirm;

	@ViewInject(R.id.tv_sbumit)
	private TextView mTv_sbumit;

	private String mStrMobile;
	private String mStrCode;
	private String mStrPwd;
	private String mStrPwdConfirm;

	private String userPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_modify_password);
		init();
	}

	private void init()
	{
		initTitle();
		registeClick();
		initSDSendValidateButton();
		showBindPhoneDialog();
	}

	public void initRequest()
	{
		RequestModel model =new RequestModel();
		userPhone = mCet_mobile.getText().toString();
		if (TextUtils.isEmpty(userPhone))
		{
			SDToast.showToast("请输入手机号码");
			return;
		}
		model.putCtl("user");
		model.putAct("check_mobile");
		model.put("mobile", userPhone);
		SDRequestCallBack<Check_MobActModel> handler = new SDRequestCallBack<Check_MobActModel>()
		{
					@Override
					public void onStart()
					{
						SDDialogManager.showProgressDialog("请稍候...");
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo)
					{
						if(actModel.getExists() == 1  || actModel.getIs_tmp() ==1)
						{
							requestSendCode();
						}else
						{
							Intent intent = new Intent(ModifyPasswordActivity.this,RegisterActivity.class);
							intent.putExtra(RegisterActivity.EXTRAS_Phone, userPhone);
							startActivity(intent);
						}
					}

					@Override
					public void onFinish()
					{
						SDDialogManager.dismissProgressDialog();
					}
				};
				InterfaceServer.getInstance().requestInterface(model, handler);
			}
	private void showBindPhoneDialog()
	{
		LocalUserModel user = AppHelper.getLocalUser();
		if (user != null)
		{
			mCet_mobile.setEnabled(false);
			String mobile = user.getUser_mobile();
			if (TextUtils.isEmpty(mobile))
			{
				// TODO 跳到绑定手机号界面
			} else
			{
				mCet_mobile.setText(mobile);
			}
		} else
		{
			mCet_mobile.setEnabled(true);
		}
	}

	/**
	 * 初始化发送验证码按钮
	 */
	private void initSDSendValidateButton()
	{
		mSvb_validate.setmListener(new SDSendValidateButtonListener()
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

	private void requestSendCode()
	{
		mStrMobile = mCet_mobile.getText().toString();
		if (TextUtils.isEmpty(mStrMobile))
		{
			SDToast.showToast("请输入手机号码");
			return;
		}

		CommonInterface.requestValidateCode(mStrMobile, 2, new SDRequestCallBack<Sms_send_sms_codeActModel>()
		{

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() > 0)
				{
					mSvb_validate.setmDisableTime(actModel.getLesstime());
					mSvb_validate.startTickWork();
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

	private void initTitle()
	{
		mTitle.setMiddleTextTop("修改密码");
	}

	private void registeClick()
	{
		mTv_sbumit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.tv_sbumit:
			clickSubmit();
			break;

		default:
			break;
		}
	}

	private void clickSubmit()
	{
		if (validateParam())
		{
			RequestModel model = new RequestModel();
			model.putCtl("user");
			model.putAct("phmodifypassword");
			model.put("mobile", mStrMobile);
			model.put("sms_verify", mStrCode);
			model.put("new_pwd", mStrPwd);
			SDRequestCallBack<User_infoModel> handler = new SDRequestCallBack<User_infoModel>()
			{

				@Override
				public void onStart()
				{
					SDDialogManager.showProgressDialog("请稍候...");
				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo)
				{
					if (actModel.getStatus() == 1)
					{
						LocalUserModel.dealLoginSuccess(actModel, false);
						finish();
					}
				}

				@Override
				public void onFinish()
				{
					SDDialogManager.dismissProgressDialog();
				}
			};
			InterfaceServer.getInstance().requestInterface(model, handler);
		}

	}

	private boolean validateParam()
	{
		mStrMobile=mCet_mobile.getText().toString();
		if (TextUtils.isEmpty(mStrMobile))
		{
			SDToast.showToast("手机号不能为空");
			return false;
		}

		mStrCode = mCet_code.getText().toString();
		if (TextUtils.isEmpty(mStrCode))
		{
			SDToast.showToast("验证码不能为空");
			return false;
		}
		mStrPwd = mCet_pwd.getText().toString();
		if (TextUtils.isEmpty(mStrPwd))
		{
			SDToast.showToast("新密码不能为空");
			return false;
		}
		mStrPwdConfirm = mCet_pwd_confirm.getText().toString();
		if (TextUtils.isEmpty(mStrPwdConfirm))
		{
			SDToast.showToast("确认新密码不能为空");
			return false;
		}

		if (!mStrPwd.equals(mStrPwdConfirm))
		{
			SDToast.showToast("两次密码不一致");
			return false;
		}

		return true;
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		switch (EnumEventTag.valueOf(event.getTagInt()))
		{
		case CONFIRM_IMAGE_CODE:
			if (SDActivityManager.getInstance().isLastActivity(this))
			{
				requestSendCode();
			}
			break;

		default:
			break;
		}
	}

}