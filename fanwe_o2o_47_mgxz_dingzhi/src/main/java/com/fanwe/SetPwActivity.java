package com.fanwe;

import com.fanwe.BaseActivity;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.dialog.SDDialogManager;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.User_InfoActModel;
import com.fanwe.model.User_RegisterAndLoginModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SetPwActivity extends BaseActivity {

	@ViewInject(R.id.et_pwd_into)
	private ClearEditText mEt_pwd_into;

	@ViewInject(R.id.et_pwd)
	private ClearEditText mEt_pwd;

	@ViewInject(R.id.tv_success)
	private TextView mTv_success;

	private String phone;

	private String code;

	private String passWord;

	private String passWordSecond;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.activity_set_pw);
		init();
	}

	private void init() {
		initGetIntent();
		initTitle();
		registeClick();
	}

	private void initGetIntent() {
		Bundle bundle = getIntent().getExtras();
		phone = bundle.getString("mobile");
		code = bundle.getString("sms_verify");
	}

	private void registeClick() {
		mTv_success.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_success:

			clickLogin();
			break;
		default:

			break;
		}

	}

	private void clickLogin() {

		if (validateParams()) {
			clickSuccess();
		}
	}

	private boolean validateParams() {
		passWord = mEt_pwd_into.getText().toString();
		if (TextUtils.isEmpty(passWord)) {
			MGToast.showToast("请输入密码");
			return false;
		}
		if (passWord.length() < 6) {
			MGToast.showToast("密码不少于6位");
			return false;
		}

		passWordSecond = mEt_pwd.getText().toString();
		if (TextUtils.isEmpty(passWordSecond)) {
			MGToast.showToast("请输入确认密码");
			return false;
		}
		return true;
	}

	private void clickSuccess() {
		RequestModel model = new RequestModel();
		if (!passWord.equals(passWordSecond)) {
			mEt_pwd_into.setText("");
			mEt_pwd.setText("");
			MGToast.showToast("输入的密码不一致");
			return;
		}
		model.putCtl("user");
		model.putAct("register_save");
		model.put("user_mob", phone);
		model.put("user_pwd", passWordSecond);
		SDRequestCallBack<User_infoModel> handler = new SDRequestCallBack<User_infoModel>() {

			@Override
			public void onStart() {
				SDDialogManager.showProgressDialog("请稍候...");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				int is_tem = actModel.getIs_tmp();
				if (is_tem != 1) {
					dealLoginNormalSuccess(actModel, true);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {

			}

			@Override
			public void onFinish() {
				SDDialogManager.dismissProgressDialog();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);
	}

	protected void dealLoginNormalSuccess(User_infoModel actModel, boolean postEvent) {
		LocalUserModel.dealLoginSuccess(actModel, postEvent);
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void initTitle() {

		mTitle.setMiddleTextTop("设置密码");

	}
}
