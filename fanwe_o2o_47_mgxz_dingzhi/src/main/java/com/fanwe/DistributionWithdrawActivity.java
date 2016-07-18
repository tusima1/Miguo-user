package com.fanwe;

import java.math.BigDecimal;
import java.util.Arrays;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.BaseActivity;
import com.fanwe.BindMobileActivity;
import com.fanwe.DistributionWithdrawLogActivity;
import com.fanwe.app.AppHelper;
import com.fanwe.common.CommonInterface;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.SDStickyScrollView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.adapter.SDSimpleTextAdapter;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.customview.SDSendValidateButton;
import com.fanwe.library.customview.SDSendValidateButton.SDSendValidateButtonListener;
import com.fanwe.library.customview.StickyScrollView;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.dialog.SDDialogMenu;
import com.fanwe.library.dialog.SDDialogMenu.SDDialogMenuListener;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.listener.NumLimitWatcher;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.Fx_WithDraw;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Sms_send_sms_codeActModel;
import com.fanwe.model.Uc_fxwithdraw_indexActModel;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;

/**
 * 提现
 * 
 * @author Administrator
 * 
 */
public class DistributionWithdrawActivity extends BaseActivity {
	@ViewInject(R.id.ll_withdraw_type)
	private LinearLayout mLl_withdraw_type;

	@ViewInject(R.id.tv_withdraw_type)
	private TextView mTv_withdraw_type;

	@ViewInject(R.id.et_money)
	private EditText mEt_money;

	@ViewInject(R.id.ll_bank_info)
	private LinearLayout mLl_bank_info;

	@ViewInject(R.id.et_bank_name)
	private EditText mEt_bank_name;

	@ViewInject(R.id.et_bank_number)
	private EditText mEt_bank_number;

	@ViewInject(R.id.et_real_name)
	private EditText mEt_real_name;

	@ViewInject(R.id.et_code)
	private EditText mEt_code;

	@ViewInject(R.id.btn_send_code)
	private SDSendValidateButton mBtn_send_code;

	@ViewInject(R.id.btn_submit)
	private Button mBtn_submit;

	@ViewInject(R.id.tv_circle)
	private TextView mTv_circle;
	
	@ViewInject(R.id.ssv_scroll)
	private ScrollView rootView;

	/** 0表示提现至余额 1表示提至银行卡 */
	private int mWithdrawType = 1;
	private String mStrMoney;
	private String mStrBankName;
	private String mStrBankNumber;
	private String mStrRealName;
	private String mStrCode;
	private String mStrMobile;
	private Float money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setmTitleType(TitleType.TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frag_distribution_withdraw);
		init();
	}

	private void init() 
	{
		initTitle();
		binData();
		registerClick();
		initMobile();
		changeViewByWithdrawType();
		hideKeyboradOnTouchOutside(DistributionWithdrawActivity.this, rootView);
	}

	private void binData() 
	{
		money = Float.parseFloat(getIntent().getStringExtra("money"));
		SDViewBinder.setTextView(mTv_circle, "可提现额（元）"+"\n"+money);
	}

	private void initMobile() {
		LocalUserModel user = AppHelper.getLocalUser();
		if (user != null) {
			mStrMobile = user.getUser_mobile();
		}
	}

	private void initSDSendValidateButton() 
	{
		mBtn_send_code.setmListener(new SDSendValidateButtonListener() {
			@Override
			public void onTick() {

			}

			@Override
			public void onClickSendValidateButton() {

				requestSendCode();
			}
		});
	}

	private void showBindMobileDialog() {
		Intent intent = new Intent(this, BindMobileActivity.class);
		startActivity(intent);
	}

	/**
	 * 请求验证码
	 */
	protected void requestSendCode() {
		CommonInterface.requestValidateCode(mStrMobile,0,
				new SDRequestCallBack<Sms_send_sms_codeActModel>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						switch (actModel.getStatus()) {
						case 0:
							showBindMobileDialog();
							break;
						case 1:
							mBtn_send_code.setmDisableTime(actModel
									.getLesstime());
							mBtn_send_code.startTickWork();
							break;

						default:
							break;
						}
					}

					@Override
					public void onStart() {
						SDDialogManager.showProgressDialog("请稍候");
					}

					@Override
					public void onFinish() {
						SDDialogManager.dismissProgressDialog();
					}

					@Override
					public void onFailure(HttpException error, String msg)
					{

					}
				});
	}

	private void registerClick() 
	{
		mLl_withdraw_type.setOnClickListener(this);
		mBtn_submit.setOnClickListener(this);
		mEt_money.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				return false;
			}
		});
		mEt_money.addTextChangedListener(new NumLimitWatcher() {
			
			@Override
			protected void afterTextChanged2(Editable s) {
				Float et_money = 0.0f;
				String number = s.toString();
				if (!number.equals(""))
				{
					et_money = Float.parseFloat(number);
					BigDecimal bd1 = new BigDecimal(money);
					bd1 = bd1.setScale(2,BigDecimal.ROUND_HALF_UP);
					Float total = Float.parseFloat(bd1+"");
					if (et_money > total || et_money == 0 || et_money < 0) {
						mEt_money.setTextColor(Color.RED);
						mBtn_send_code
								.setBackgroundResource(R.drawable.my_tixian_sendcode);
						mBtn_send_code.setmTextEnable("");
						mBtn_send_code.setClickable(false);
						mBtn_send_code.stopTickWork();
					} else {
						mEt_money.setTextColor(Color.parseColor("#4C4C4C"));
						mBtn_send_code
								.setBackgroundResource(R.drawable.selector_main_color_corner);
						GradientDrawable gradientDrawable = new GradientDrawable();
						gradientDrawable.setStroke(SDViewUtil.dp2px(1),
								mBtn_send_code.getmTextColorEnable());
						gradientDrawable.setCornerRadius(SDViewUtil.dp2px(5));
						SDViewUtil.setBackgroundDrawable(mBtn_send_code,
								gradientDrawable);
						mBtn_send_code.setmTextEnable("获取验证码");
						mBtn_send_code.setClickable(true);
						mBtn_send_code.setmBackgroundEnableResId(0);
						mBtn_send_code.updateViewState(true);
						initSDSendValidateButton();
					}
				}
			}
		});
	}

	private void initTitle() {
		mTitle.setMiddleTextTop("提现");

		mTitle.initRightItem(1);

		mTitle.getItemRight(0).setTextBot("提现日志");

	}

	@Override
	public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
		clickWithdrawLog();
	}

	/**
	 * 提现日志
	 */
	private void clickWithdrawLog()
	{
		// TODO 跳到提现日志界面
		Intent intent = new Intent(this, DistributionWithdrawLogActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (v == mLl_withdraw_type) {
			clickWithDrawType();
		} else if (v == mBtn_submit) {
			clickSubmit();
		}
	}

	private void clickSubmit() {
		// TODO 请求提现接口
		if (!validateParams()) {
			return;
		}

		RequestModel model = new RequestModel();
		model.putCtl("fxwithdraw");
		model.putAct("commit");
		model.put("money", mStrMoney);
		model.put("type", mWithdrawType);
		model.put("bank_name", mStrBankName);
		model.put("bank_account", mStrBankNumber);
		model.put("bank_user", mStrRealName);
		model.put("code", mStrCode);

		InterfaceServer.getInstance().requestInterface(model,
				new SDRequestCallBack<BaseActModel>() {
					@Override
					public void onStart() {
						SDDialogManager.showProgressDialog("");
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) 
					{
						if (actModel.getStatus() == 1) {
							// 提交成功
							mEt_code.setText("");
							Intent intent = new Intent(DistributionWithdrawActivity.this,
									DistributionWithdrawLogActivity.class);
							startActivity(intent);
							finish();
						} 
					}

					@Override
					public void onFinish() {
						SDDialogManager.dismissProgressDialog();
					}
				});

	}

	private boolean validateParams() {
		mStrMoney = mEt_money.getText().toString().trim();
		if (isEmpty(mStrMoney)) {
			SDToast.showToast("请输入提现金额");
			return false;
		}

		switch (mWithdrawType) {
		case 0:
			mStrBankName = null;
			mStrBankNumber = null;
			mStrRealName = null;
			break;
		case 1:
			mStrBankName = mEt_bank_name.getText().toString().trim();
			if (isEmpty(mStrBankName)) {
				SDToast.showToast("请输入开户行名称");
				return false;
			}

			mStrBankNumber = mEt_bank_number.getText().toString().trim();
			if (isEmpty(mStrBankNumber)) {
				SDToast.showToast("请输入银行卡号");
				return false;
			}

			mStrRealName = mEt_real_name.getText().toString().trim();
			if (isEmpty(mStrRealName)) {
				SDToast.showToast("请输入姓名");
				return false;
			}
			break;

		default:
			break;
		}

		mStrCode = mEt_code.getText().toString().trim();
		if (isEmpty(mStrCode)) {
			SDToast.showToast("请输入验证码");
			return false;
		}
		return true;
	}

	/**
	 * 点击提现类型
	 */
	private void clickWithDrawType() {
		// TODO 弹出dialog选择提现类型
		SDDialogMenu dialog = new SDDialogMenu(this);
		String[] arrType = new String[] { "银行卡" };
		final SDSimpleTextAdapter<String> adapter = new SDSimpleTextAdapter<String>(
				Arrays.asList(arrType), this);
		dialog.setmListener(new SDDialogMenuListener() {

			@Override
			public void onItemClick(View v, int index, SDDialogMenu dialog) {
				switch (index) {
				case 0:
					mWithdrawType = 1;
					break;

				default:
					break;
				}
				changeViewByWithdrawType();
			}

			@Override
			public void onDismiss(SDDialogMenu dialog) {

			}

			@Override
			public void onCancelClick(View v, SDDialogMenu dialog) {
			}
		});
		dialog.setAdapter(adapter);
		dialog.showBottom();
	}

	protected void changeViewByWithdrawType() {
		switch (mWithdrawType) {
		case 0:
			mTv_withdraw_type.setText("银行卡");
			SDViewUtil.hide(mLl_bank_info);
		case 1:
			mTv_withdraw_type.setText("银行卡");
			SDViewUtil.show(mLl_bank_info);
			break;

		default:
			break;
		}

	}

	@Override
	public void onDestroy() {
		mBtn_send_code.stopTickWork();
		super.onDestroy();
	}

	@Override
	public void onEventMainThread(SDBaseEvent event) {
		super.onEventMainThread(event);
		switch (EnumEventTag.valueOf(event.getTagInt())) {
		case BIND_MOBILE_SUCCESS:
			initMobile();
			break;
		case CONFIRM_IMAGE_CODE:
			if (SDActivityManager.getInstance().isLastActivity(this)) {
				requestSendCode();
			}
			break;
		default:
			break;
		}
	}
}
