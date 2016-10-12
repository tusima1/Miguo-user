package com.fanwe.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.MD5Util;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.presents.LoginHelper;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;

public class LoginFragment extends LoginBaseFragment {

    @ViewInject(R.id.act_login_et_email)
    private ClearEditText mEtEmail;

    @ViewInject(R.id.act_login_et_pwd)
    private ClearEditText mEtPwd;

    @ViewInject(R.id.act_login_tv_login)
    private TextView mTvLogin;


    private String mStrUserName;
    private String mStrPassword;
    LoginHelper mLoginHelper;


    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_login);
    }

    @Override
    protected void init() {
        super.init();
        registeClick();
        mLoginHelper = new LoginHelper(getActivity(), getActivity(), this);
    }


    private void registeClick() {
        mTvLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.act_login_tv_login:
                doLogin();
                break;

            default:
                break;
        }
    }


    private int count;
    private boolean showToast = false;

    public void doLogin() {
        if (validateParam()) {
            mTvLogin.setEnabled(false);
            count++;
            if (count >= 4) {
                if (!showToast) {
                    showToast = true;
                    MGToast.showToast("操作过于频繁,请稍候再试!");
                    MGUIUtil.runOnUiThreadDelayed(new Runnable() {
                        @Override
                        public void run() {
                            count = 0;
                            showToast = false;
                        }
                    }, 10000);
                }
                return;
            }
            mLoginHelper.doLogin(mStrUserName, MD5Util.MD5(mStrPassword), 0,mTvLogin);
            SDDialogManager.showProgressDialog("请稍候...");
        }
    }

    private boolean validateParam() {
        mStrUserName = mEtEmail.getText().toString();
        if (TextUtils.isEmpty(mStrUserName)) {
            MGToast.showToast("请输入手机号");
            mEtEmail.requestFocus();
            return false;
        }
        if (mStrUserName.length() != 11) {
            MGToast.showToast("请输入正确的手机号");
            mEtEmail.requestFocus();
            return false;
        }
        mStrPassword = mEtPwd.getText().toString();
        if (TextUtils.isEmpty(mStrPassword)) {
            MGToast.showToast("密码不能为空");
            mEtPwd.requestFocus();
            return false;
        }
        if (mStrPassword.length() < 6) {
            MGToast.showToast("密码不能小于6位");
            mEtPwd.requestFocus();
            return false;
        }
        if (mStrPassword.length() > 20) {
            MGToast.showToast("密码不能大于20位");
            mEtPwd.requestFocus();
            return false;
        }
        return true;
    }


}