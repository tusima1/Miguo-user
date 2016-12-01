package com.miguo.category.fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.constant.ServerUrl;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.MD5Util;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.UserInfoNew;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiLoginActivity;
import com.miguo.dao.IMLoginDao;
import com.miguo.dao.IMUserInfoDao;
import com.miguo.dao.LoginByMobileDao;
import com.miguo.dao.TencentSignDao;
import com.miguo.dao.impl.IMLoginDaoImpl;
import com.miguo.dao.impl.IMUserInfoDaoImpl;
import com.miguo.dao.impl.LoginByMobileDaoImpl;
import com.miguo.dao.impl.TencentSignDaoImpl;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.listener.fragment.HiLoginByMobileFragmentListener;
import com.miguo.live.model.generateSign.ModelGenerateSign;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.BaseUtils;
import com.miguo.view.IMLoginView;
import com.miguo.view.IMUserInfoView;
import com.miguo.view.LoginByMobileView;
import com.miguo.view.TencentSignView;
import com.tencent.qcloud.suixinbo.avcontrollers.QavsdkControl;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;

/**
 * Created by zlh on 2016/12/1.
 * 根据手机号码密码登录
 */
public class HiLoginByMobileFragmentCategory extends FragmentCategory implements LoginByMobileView, TencentSignView, IMLoginView, IMUserInfoView{

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
    /**
     * 腾讯获取签名
     */
    TencentSignDao tencentSignDao;
    /**
     * IM登录接口
     */
    IMLoginDao imLoginDao;
    /**
     * 绑定用户信息到IM接口
     */
    IMUserInfoDao imUserInfoDao;

    public HiLoginByMobileFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void initFirst() {
        loginByMobileDao = new LoginByMobileDaoImpl(this);
        tencentSignDao = new TencentSignDaoImpl(this);
        imLoginDao = new IMLoginDaoImpl(this);
        imUserInfoDao = new IMUserInfoDaoImpl(this);
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

    }

    public void clickLogin(){
        if (validateParam()) {
            mTvLogin.setEnabled(false);
            /**
             * {@link #loginSuccess(UserInfoNew, String, String)}
             * {@link #loginError(String)}
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
     * 获取腾讯签名
     *
     * @param token
     */
    private void handlerTencentSign(String token) {
        if (TextUtils.isEmpty(token)) {
            Log.d(tag, "handler tencent sign token is null...");
            return;
        }
        /**
         * {@link #getTencentSignSuccess(ModelGenerateSign)}
         * {@link #getTencentSignError()}
         */
        tencentSignDao.getTencentSign(token);
    }

    /**
     * IM登录
     * @param userId
     * @param usersig
     */
    private void handlerIMLogin(String userId, String usersig) {
        /**
         * {@link #imLoginSuccess()}
         * {@link #imLoginError(String)}
         */
        imLoginDao.imLogin(userId, usersig);
    }

    /**
     * 登录回调
     */
    @Override
    public void loginError(String message) {
        SDDialogManager.dismissProgressDialog();
        mTvLogin.setEnabled(true);
    }

    @Override
    public void loginSuccess(UserInfoNew user, String mobile, String password) {
        SDDialogManager.dismissProgressDialog();
        mTvLogin.setEnabled(true);
        /**
         * 保存用户信息到本地
         */
//        saveUserToLocal(user, mobile, password);
        /**
         * 获取腾讯sign签名
         */
        handlerTencentSign(App.getApplication().getToken());
        /**
         * 保存用户信息SharedPreferences
         */
//        handlerSaveUser(mobile, password);
//        initJpush();
        finishActivity();
    }

    private void finishActivity(){
        if(getActivity() instanceof HiLoginActivity){
            ((HiLoginActivity)getActivity()).finishActivity();
            return;
        }
        BaseUtils.finishActivity(getActivity());
    }

    /**
     * 获取腾讯签名
     * 获取成功后需要调用IM登录
     */
    @Override
    public void getTencentSignSuccess(ModelGenerateSign sign) {
        if (null == sign) {
            return;
        }
        String usersig = sign.getUsersig();
        App.getInstance().setUserSign(usersig);
        MySelfInfo.getInstance().setUserSig(usersig);
        App.getInstance().setUserSign(usersig);
        String userId = MySelfInfo.getInstance().getId();

        if (TextUtils.isEmpty(userId)) {
            UserInfoNew currentInfo = App.getInstance().getCurrentUser();
            if (currentInfo != null) {
                userId = currentInfo.getUser_id();
            } else {
                return;
            }
        }
        handlerIMLogin(userId, usersig);
    }

    @Override
    public void getTencentSignError() {
        Log.d(tag, "get tencent sign error..");
    }

    /**
     * IM登陆回调
     */
    @Override
    public void imLoginError(String message) {
        Log.d(tag, "im login error and the message is: " + message);
    }

    /**
     * IM登录成功
     * 登录成功后要将用户名和头像绑定到IM
     */
    @Override
    public void imLoginSuccess() {
        if (!TextUtils.isEmpty(App.getInstance().getToken())) {
            /**
             * 不需要回调
             */
            imUserInfoDao.updateTencentNickName(App.getInstance().getCurrentUser().getNick());
            imUserInfoDao.updateTencentAvatar(App.getInstance().getCurrentUser().getIcon());
        }
        /**
         * 开始直播AVSDK
         */
        startAVSDK();
        App.getInstance().setImLoginSuccess(true);
    }

    /**
     * 初始化AVSDK
     */
    public void startAVSDK() {
        String userid = MySelfInfo.getInstance().getId();
        String userSign = MySelfInfo.getInstance().getUserSig();
        int appId = Constants.SDK_APPID;
        int ccType = Constants.ACCOUNT_TYPE;
        if (ServerUrl.DEBUG) {
            appId = Constants.SDK_APPID_TEST;
            ccType = Constants.ACCOUNT_TYPE_Test;
        }
        QavsdkControl.getInstance().setAvConfig(appId, ccType + "", userid, userSign);
        QavsdkControl.getInstance().startContext();

    }

}
