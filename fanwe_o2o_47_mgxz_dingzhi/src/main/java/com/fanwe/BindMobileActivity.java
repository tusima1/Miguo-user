package com.fanwe;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.constant.EnumEventTag;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.customview.SDSendValidateButton;
import com.fanwe.library.customview.SDSendValidateButton.SDSendValidateButtonListener;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;
import com.sunday.eventbus.SDBaseEvent;

/**
 * 绑定手机号
 * 
 * @author Administrator
 * 
 */
public class BindMobileActivity extends BaseActivity
{

	@ViewInject(R.id.et_mobile)
	private EditText mEt_mobile;

	@ViewInject(R.id.et_code)
	private EditText mEt_code;

	@ViewInject(R.id.btn_send_code)
	private SDSendValidateButton mBtn_send_code;

	@ViewInject(R.id.tv_submit)
	private TextView mTv_submit;

	private String mStrMobile;
	private String mStrCode;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_bind_mobile);
		init();
	}

	private void init()
	{
		initTitle();
		initSDSendValidateButton();
		register();
	}

	private void register()
	{
		mTv_submit.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				clickSubmit();
			}
		});
	}

	protected void clickSubmit()
	{
		if (validateParams())
		{
		}
	}

	private boolean validateParams()
	{
		if (TextUtils.isEmpty(mStrMobile))
		{
			MGToast.showToast("手机号不能为空");
			return false;
		}
		mStrCode = mEt_code.getText().toString();
		if (TextUtils.isEmpty(mStrCode))
		{
			MGToast.showToast("验证码不能为空");
			return false;
		}

		return true;
	}

	private void initSDSendValidateButton()
	{
		mBtn_send_code.setmListener(new SDSendValidateButtonListener()
		{
			@Override
			public void onTick()
			{
			}

			@Override
			public void onClickSendValidateButton()
			{
				requestSendCode();
			}
		});

	}

	/**
	 * 发送验证码
	 */
	protected void requestSendCode()
	{
		mStrMobile = mEt_mobile.getText().toString();
		if (TextUtils.isEmpty(mStrMobile))
		{
			MGToast.showToast("请输入手机号码");
			return;
		}

	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop("绑定手机");
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
