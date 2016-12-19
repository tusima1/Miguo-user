package com.miguo.category.fragment;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.model.Check_MobActModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.UserInfoNew;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiLoginActivity;
import com.miguo.category.HiLoginCategory;
import com.miguo.dao.CheckMobileExistDao;
import com.miguo.dao.GetSMSCodeDao;
import com.miguo.dao.LoginByMobilleWithSMSDao;
import com.miguo.dao.impl.CheckMobileExistDaoImpl;
import com.miguo.dao.impl.GetSMSCodeDaoImpl;
import com.miguo.dao.impl.LoginByMobilleWithSMSDaoImpl;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.listener.fragment.HiLoginQuickByMobileFragmentListener;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.view.CheckMobileExistView;
import com.miguo.view.GetSMSCodeView;
import com.miguo.view.LoginByMobilleWithSMSView;

/**
 * Created by zlh on 2016/12/1.
 */

public class HiLoginQuickByMobileFragmentCategory extends FragmentCategory implements LoginByMobilleWithSMSView, GetSMSCodeView, CheckMobileExistView{

    @ViewInject(R.id.et_mobile)
    private ClearEditText mEtMobile;

    @ViewInject(R.id.et_code)
    private ClearEditText mEtCode;

    @ViewInject(R.id.btn_send_code)
    private Button mBtnSendCode;

    @ViewInject(R.id.btn_login)
    private Button mBtnLogin;

    private String mStrCode;

    protected Check_MobActModel mActModel;

    private String mNumberPhone;

    private TimeCount time;

    /**
     * 接口回调
     * {@link com.miguo.dao.impl.LoginByMobilleWithSMSDaoImpl}
     * {@link com.miguo.view.LoginByMobilleWithSMSView}
     */
    LoginByMobilleWithSMSDao loginByMobilleWithSMSDao;
    /**
     * {@link com.miguo.dao.impl.GetSMSCodeDaoImpl}
     * {@link com.miguo.view.GetSMSCodeView}
     * {@link #getSMSCodeSuccess(String)}
     */
    GetSMSCodeDao getSMSCodeDao;
    /**
     * {@link com.miguo.dao.impl.CheckMobileExistDaoImpl}
     * {@link com.miguo.view.CheckMobileExistView}
     * {@link #mobileExist()}
     * {@link #mobileDoesNotExist(String)}
     */
    CheckMobileExistDao checkMobileExistDao;


    public HiLoginQuickByMobileFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void initFirst() {
        loginByMobilleWithSMSDao = new LoginByMobilleWithSMSDaoImpl(this);
        getSMSCodeDao = new GetSMSCodeDaoImpl(this);
        checkMobileExistDao = new CheckMobileExistDaoImpl(this);
    }

    @Override
    protected void findFragmentViews() {
        ViewUtils.inject(this, view);
    }

    @Override
    protected void initFragmentListener() {
        listener = new HiLoginQuickByMobileFragmentListener(this);
    }

    @Override
    protected void setFragmentListener() {
        mBtnSendCode.setOnClickListener(listener);
        mBtnLogin.setOnClickListener(listener);
    }

    @Override
    protected void init() {
        initUserMobile();
        initTimer();
    }

    /**
     * 如果是从注册界面过来的要还原手机号码
     */
    private void initUserMobile(){
        mEtMobile.setText(null != getActivity() ? getActivity().getUserMobile() : "");
    }

    private void initTimer(){
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
    }

    public void clickSendSMSCode(){
        mNumberPhone = mEtMobile.getText().toString();
        if (TextUtils.isEmpty(mNumberPhone)) {
            MGToast.showToast("请输入手机号码!");
            return;
        }
        if (mNumberPhone.length() != 11) {
            MGToast.showToast("请输入正确的手机号码");
            return;
        }
        /**
         * {@link com.miguo.dao.impl.CheckMobileExistDaoImpl}
         * {@link com.miguo.view.CheckMobileExistView}
         * {@link #mobileExist()}
         * {@link #mobileDoesNotExist(String)}
         */
        checkMobileExistDao.checkMobileExist(mNumberPhone);
        SDDialogManager.showProgressDialog("请稍候...");
    }

    public void clickLogin(){
        mNumberPhone = mEtMobile.getText().toString();
        if (TextUtils.isEmpty(mNumberPhone)) {
            MGToast.showToast("请输入手机号码!");
            return;
        }
        if (mNumberPhone.length() != 11) {
            MGToast.showToast("请输入正确的手机号码");
            return;
        }
        mStrCode = mEtCode.getText().toString();
        if (TextUtils.isEmpty(mStrCode)) {
            MGToast.showToast("请输入验证码!");
            return;
        }
        mBtnLogin.setEnabled(false);
        /**
         * 接口回调
         * {@link com.miguo.dao.impl.LoginByMobilleWithSMSDaoImpl}
         * {@link com.miguo.view.LoginByMobilleWithSMSView}
         * {@link #loginByMobileWithSMSSuccess(UserInfoNew)}
         * {@link #loginByMobileWithSMSError(String)}
         */
        loginByMobilleWithSMSDao.loginByMobileWithSMS(mNumberPhone, mStrCode, getShareCode());
        SDDialogManager.showProgressDialog("请稍候...");
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            mBtnSendCode.setText("重新验证");
            mBtnSendCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mBtnSendCode.setClickable(false);
            mBtnSendCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }

    public String getShareCode(){
        return ((HiLoginActivity)getActivity()).getCategory().getShareCode();
    }

    /**
     * 不管手机号存不存在，都要注册
     * @param message
     */
    @Override
    public void mobileDoesNotExist(String message) {
//        showToast("手机号不存在");
        SDDialogManager.dismissProgressDialog();
        mobileExist();
    }

    @Override
    public void mobileExist() {
        /**
         * {@link com.miguo.dao.impl.GetSMSCodeDaoImpl}
         * {@link com.miguo.view.GetSMSCodeView}
         * {@link #getSMSCodeSuccess(String)}
         */
        getSMSCodeDao.getSMSCodeForQuickLoginAndBindMobile(mNumberPhone);
        time.start();
    }

    @Override
    public void getSMSCodeSuccess(String message) {
        showToast(message);
        SDDialogManager.dismissProgressDialog();
    }

    @Override
    public void getSMSCodeError(String message) {
        showToast(message);
        mBtnSendCode.setText("重新发送验证码");
        time.onFinish();
        SDDialogManager.dismissProgressDialog();
    }

    @Override
    public void loginByMobileWithSMSError(String message) {
        SDDialogManager.dismissProgressDialog();
        mBtnLogin.setEnabled(true);
        showToast(message);
    }

    @Override
    public void loginByMobileWithSMSSuccess(UserInfoNew userInfoNew) {
        SDDialogManager.dismissProgressDialog();
        mBtnLogin.setEnabled(true);
        handleLoginSuccess(userInfoNew);
    }

    private void handleLoginSuccess(UserInfoNew user){
        if(null != getHiLoginCategory()){
            getHiLoginCategory().handleLoginSuccess(user);
        }
    }

    public HiLoginCategory getHiLoginCategory(){
        if(getActivity() instanceof HiLoginActivity){
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
