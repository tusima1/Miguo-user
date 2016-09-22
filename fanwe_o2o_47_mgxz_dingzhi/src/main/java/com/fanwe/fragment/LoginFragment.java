package com.fanwe.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        mLoginHelper = new LoginHelper(getActivity(),getActivity(), this);
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
    private boolean showToast=false;
    public void doLogin(){
        if (validateParam()) {
            if (TextUtils.isEmpty(mStrUserName)) {
                Toast.makeText(getActivity(), "name can not be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mStrPassword)) {
                Toast.makeText(getActivity(), "password can not be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            count++;
            if (count>=4){
                if (!showToast){
                    showToast=true;
                    MGToast.showToast("操作过于频繁,请稍候再试!");
                    MGUIUtil.runOnUiThreadDelayed(new Runnable() {
                        @Override
                        public void run() {
                            count=0;
                            showToast=false;
                        }
                    },10000);
                }
                return;
            }
            mLoginHelper.doLogin(mStrUserName, MD5Util.MD5(mStrPassword), 0);
            SDDialogManager.showProgressDialog("请稍候...");
        }
    }

    private boolean validateParam() {
        mStrUserName = mEtEmail.getText().toString();
        if (TextUtils.isEmpty(mStrUserName)) {
            MGToast.showToast("请输入账号");
            mEtEmail.requestFocus();
            return false;
        }
        mStrPassword = mEtPwd.getText().toString();
        if (TextUtils.isEmpty(mStrPassword)) {
            MGToast.showToast("密码不能为空");
            mEtPwd.requestFocus();
            return false;
        }
        return true;
    }


}