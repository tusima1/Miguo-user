package com.miguo.category;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.base.CommonHelper;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.model.Check_MobActModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.UserInfoNew;
import com.fanwe.user.presents.LoginHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.app.HiRegisterActivity;
import com.miguo.dao.CheckMobileExistDao;
import com.miguo.dao.GetSMSCodeDao;
import com.miguo.dao.RegisterByMobileDao;
import com.miguo.dao.impl.CheckMobileExistDaoImpl;
import com.miguo.dao.impl.GetSMSCodeDaoImpl;
import com.miguo.dao.impl.RegisterByMobileDaoImpl;
import com.miguo.definition.ClassPath;
import com.miguo.definition.IntentKey;
import com.miguo.factory.ClassNameFactory;
import com.miguo.listener.HiRegisterListener;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.BaseUtils;
import com.miguo.view.CheckMobileExistView;
import com.miguo.view.GetSMSCodeView;
import com.miguo.view.RegisterByMobileView;

/**
 * Created by zlh on 2016/12/3.
 * 注册界面
 */
public class HiRegisterCategory extends Category implements CheckMobileExistView, GetSMSCodeView,RegisterByMobileView{

    @ViewInject(R.id.et_userphone)
    private ClearEditText mEtUserphone;

    /**
     * 验证码。
     */
    @ViewInject(R.id.et_pwd)
    private ClearEditText mEtPwd;

    @ViewInject(R.id.btn_send_code)
    private Button mBt_send_code;

    @ViewInject(R.id.ch_register)
    private CheckBox mCh_register;

    @ViewInject(R.id.ll_register_xieyi)
    private LinearLayout mLl_xieyi;

    @ViewInject(R.id.tv_register)
    private TextView mTvRegister;

    /**
     * 密码。
     */

    @ViewInject(R.id.et_pwd_into)
    private ClearEditText mEt_pwd_into;

    /**
     * 确认手机号。
     */
    @ViewInject(R.id.pwd)
    private ClearEditText pwd;


    @ViewInject(R.id.passline1)
    private LinearLayout passline1;
    @ViewInject(R.id.passline2)
    private LinearLayout passline2;

    protected static String EXTRAS_Phone = "extras_phone";

    /**
     * 验证码。
     */
    private String smsCode;
    /**
     * 密码。
     */
    private String passwordStr;
    protected Check_MobActModel mActModel;
    CommonHelper mFragmentHelper;
    LoginHelper mLoginHelper;
    private TimeCount time;

    /**
     * 检查验证码是否存在
     * {@link com.miguo.dao.impl.CheckMobileExistDaoImpl}
     * {@link com.miguo.view.CheckMobileExistView}
     * {@link #mobileExist()}
     * {@link #mobileDoesNotExist(String)}
     */
    CheckMobileExistDao checkMobileExistDao;

    /**
     * 发送短信验证码
     * {@link com.miguo.dao.impl.GetSMSCodeDaoImpl}
     * {@link com.miguo.view.GetSMSCodeView}
     * {@link #getSMSCodeSuccess(String)}
     */
    GetSMSCodeDao getSMSCodeDao;

    /**
     * 通过手机号码注册
     * {@link com.miguo.dao.impl.RegisterByMobileDaoImpl}
     * {@link com.miguo.view.RegisterByMobileView}
     * {@link #registerByMobileSuccess(UserInfoNew)}
     * {@link #registerByMobileError(String)}
     */
    RegisterByMobileDao registerByMobileDao;

    public HiRegisterCategory(HiBaseActivity activity) {
        super(activity);
    }


    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }

    @Override
    protected void initFirst() {
        checkMobileExistDao = new CheckMobileExistDaoImpl(this);
        getSMSCodeDao = new GetSMSCodeDaoImpl(this);
        registerByMobileDao = new RegisterByMobileDaoImpl(this);
    }

    @Override
    protected void initThisListener() {
        listener = new HiRegisterListener(this);
    }

    @Override
    protected void setThisListener() {
        mBt_send_code.setOnClickListener(listener);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initViews() {

    }

    public void clickRegister(){
        if (validateParam()) {
            /**
             * 第一次使用第三方登录注册
             */
            if (!TextUtils.isEmpty(getActivity().getOpenid())) {
                handleRegisterWithThirdLogin();
                return;
            }
            handleRegister();
        }
    }

    /**
     * 发送短信验证码
     */
    public void clickSendSMSCode(){
        if (TextUtils.isEmpty(getUserMobile())) {
            MGToast.showToast("请输入手机号码");
            return;
        }
        if (getUserMobile().length() != 11) {
            MGToast.showToast("请输入正确的手机号码");
            return;
        }
        /**
         * {@link com.miguo.dao.impl.CheckMobileExistDaoImpl}
         * {@link com.miguo.view.CheckMobileExistView}
         * {@link #mobileExist()}
         * {@link #mobileDoesNotExist(String)}
         */
        checkMobileExistDao.checkMobileExist(getUserMobile());
        SDDialogManager.showProgressDialog("请稍候...");
    }

    private void handleRegisterWithThirdLogin(){
        mLoginHelper.doThirdRegister(userPhone, openid, smsCode, icon, nick, platform,shareId);
    }

    private void handleRegister(){
        registerByMobileDao.registerByMobile(getUserMobile(), smsCode, passwordStr, getActivity().getShareId());
    }

    /**
     * 参数有效性判断。
     *
     * @return
     */
    private boolean validateParam() {
        String userPhone = mEtUserphone.getText().toString();
        if (TextUtils.isEmpty(userPhone)) {
            MGToast.showToast("手机号不能为空");
            return false;
        }
        if (userPhone.length() != 11) {
            MGToast.showToast("请输入正确的手机号");
            return false;
        }
        smsCode = mEtPwd.getText().toString();
        if (TextUtils.isEmpty(smsCode)) {
            MGToast.showToast("验证码不能为空");
            return false;
        }
        if (mCh_register.isChecked()) {
            MGToast.showToast("请先同意用户注册协议");
            return false;
        }
        if (!TextUtils.isEmpty(getActivity().getOpenid())) {
            return true;
        }
        String pwd2 = pwd.getText().toString();
        String pwd1 = mEt_pwd_into.getText().toString();
        if (TextUtils.isEmpty(pwd1)) {
            MGToast.showToast("密码不能为空");
            return false;
        }
        if (pwd1.length() < 6) {
            MGToast.showToast("密码不能小于6位");
            return false;
        }
        if (pwd1.length() > 20) {
            MGToast.showToast("密码不能大于20位");
            return false;
        }
        if (!pwd1.equals(pwd2)) {
            MGToast.showToast("两次密码输入不一致");
            return false;
        }
        passwordStr = pwd1;
        return true;
    }

    @Override
    public void mobileDoesNotExist(String message) {
        handleSendSMSCodeForRegister();
    }

    @Override
    public void mobileExist() {
        /**
         * 用户手机号码存在，有两种情况
         * 1：不是第三方登录后未注册(正常逻辑点的注册)，应该跳转到登录界面
         * 2：第三方登录后注册 直接获取验证码绑定手机
         */
        /**
         * case 1:
         */
        if(TextUtils.isEmpty(getActivity().getOpenid())){
            handleUserCheckExistForRegister();
            return;
        }
        handleUserCheckExistForThirdLogin();
    }

    /**
     * 检查手机号码存在，跳转到登录页
     * 常规注册
     */
    private void handleUserCheckExistForRegister(){
        MGToast.showToast("手机号已注册，请直接登录");
        if(isFromDiamond()){
            BaseUtils.finishActivity(getActivity());
            return;
        }
        SDDialogManager.dismissProgressDialog();
        goLogin();
    }

    /**
     * 跳转到登录页
     */
    private void goLogin(){
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.LOGIN_ACTIVITY));
        intent.putExtra(IntentKey.LOGIN_MOBILE, getUserMobile());
        /**
         * 选中登录页选卡为快速登录
         */
        intent.putExtra(IntentKey.EXTRA_SELECT_TAG_INDEX, 1);
        BaseUtils.jumpToNewActivityWithFinish(getActivity(), intent);
    }

    /**
     * 检查到手机号码存在
     * 发送验证码第三方绑定账号
     */
    private void handleUserCheckExistForThirdLogin(){
        handleSendSMSCodeForThirdLoginBindMobile();
    }

    /**
     * 发送验证码第三方绑定账号
     */
    private void handleSendSMSCodeForThirdLoginBindMobile(){
        /**
         * {@link com.miguo.dao.impl.GetSMSCodeDaoImpl}
         * {@link com.miguo.view.GetSMSCodeView}
         * {@link #getSMSCodeSuccess(String)}
         */
        getSMSCodeDao.getSMSCodeForQuickLoginAndBindMobile(getUserMobile());
        time.start();
    }

    /**
     * 发送验证码注册
     */
    private void handleSendSMSCodeForRegister(){
        /**
         * {@link com.miguo.dao.impl.GetSMSCodeDaoImpl}
         * {@link com.miguo.view.GetSMSCodeView}
         * {@link #getSMSCodeSuccess(String)}
         * {@link #getSMSCodeError(String)}
         */
        getSMSCodeDao.getSMSCodeForRegister(getUserMobile());
        time.start();
    }

    /**
     * 获取验证码成功
     * @param message
     */
    @Override
    public void getSMSCodeSuccess(String message) {
        showToast(message);
        SDDialogManager.dismissProgressDialog();
    }

    /**
     * 获取验证码失败
     * @param message
     */
    @Override
    public void getSMSCodeError(String message) {
        showToast(message);
        mBt_send_code.setText("重新发送验证码");
        time.onFinish();
        SDDialogManager.dismissProgressDialog();
    }

    @Override
    public void registerByMobileError(String message) {

    }

    @Override
    public void registerByMobileSuccess(UserInfoNew user) {

    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            mBt_send_code.setText("重新验证");
            mBt_send_code.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mBt_send_code.setClickable(false);
            mBt_send_code.setText(millisUntilFinished / 1000 + "秒");
        }
    }

    public String getUserMobile(){
        try{
            return mEtUserphone.getText().toString();
        }catch (Exception e){
            return "";
        }
    }

    public boolean isFromDiamond() {
        return getActivity().isFromDiamond();
    }

    @Override
    public HiRegisterActivity getActivity() {
        return (HiRegisterActivity) super.getActivity();
    }
}
