package com.miguo.category.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.MD5Util;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.UserInfoNew;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiLoginActivity;
import com.miguo.category.HiLoginCategory;
import com.miguo.dao.LoginByMobileDao;
import com.miguo.dao.impl.LoginByMobileDaoImpl;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.listener.fragment.HiLoginByMobileFragmentListener;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.NetWorkStateUtil;
import com.miguo.view.LoginByMobileView;

/**
 * Created by zlh on 2016/12/1.
 * 根据手机号码密码登录
 */
public class HiLoginByMobileFragmentCategory extends FragmentCategory implements LoginByMobileView{

    @ViewInject(R.id.act_login_et_email)
    private ClearEditText mEtEmail;

    @ViewInject(R.id.act_login_et_pwd)
    private ClearEditText mEtPwd;

    @ViewInject(R.id.act_login_tv_login)
    private TextView mTvLogin;

    private String mStrUserName;
    private String mStrPassword;

    /**
     * 登录接口
     */
    LoginByMobileDao loginByMobileDao;


    public HiLoginByMobileFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void initFirst() {
        loginByMobileDao = new LoginByMobileDaoImpl(this);
    }

    @Override
    protected void findFragmentViews() {
        ViewUtils.inject(this, view);
    }

    @Override
    protected void initFragmentListener() {
        listener = new HiLoginByMobileFragmentListener(this);
    }

    @Override
    protected void setFragmentListener() {
        mTvLogin.setOnClickListener(listener);
    }

    @Override
    protected void init() {
        initUserMobile();
    }

    /**
     * 如果是从注册界面过来的要还原手机号码
     */
    private void initUserMobile(){
        mEtEmail.setText(null != getActivity() ? getActivity().getUserMobile() : "");
    }

    public void clickLogin(){
        if (!NetWorkStateUtil.isConnected(getActivity())){
            MGToast.showToast("网络异常");
            return;
        }
        if (validateParam()) {
            mTvLogin.setEnabled(false);
            /**
             * {@link #loginSuccess(UserInfoNew)}
             * {@link #loginError(String)}
             * {@link com.miguo.dao.impl.LoginByMobileDaoImpl}
             * {@link com.miguo.view.LoginByMobileView}
             */
            loginByMobileDao.loginByMobile(mStrUserName, MD5Util.MD5(mStrPassword));
            SDDialogManager.showProgressDialog("请稍候...");
        }
    }

    /**
     * 验证输入的字符是否合法有效
     * @return
     */
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

    /**
     * 登录回调
     */
    @Override
    public void loginError(String message) {
        showToast(message);
        SDDialogManager.dismissProgressDialog();
        mTvLogin.setEnabled(true);
    }

    @Override
    public void loginSuccess(UserInfoNew user) {
        SDDialogManager.dismissProgressDialog();
        mTvLogin.setEnabled(true);
        handleLoginSuccess(user);
    }

    private void handleLoginSuccess(UserInfoNew user){
        if(null != getHiLoginCategory()){
            getHiLoginCategory().handleLoginSuccess(user);
        }
    }

    public HiLoginCategory getHiLoginCategory(){
        if(getActivity() instanceof HiLoginActivity && null != getActivity()){
            return getActivity().getCategory();
        }
        return null;
    }

    @Override
    public HiLoginActivity getActivity() {
        if(null != super.getActivity() && super.getActivity() instanceof HiLoginActivity){
            return (HiLoginActivity)super.getActivity();
        }
        return null;
    }
}
