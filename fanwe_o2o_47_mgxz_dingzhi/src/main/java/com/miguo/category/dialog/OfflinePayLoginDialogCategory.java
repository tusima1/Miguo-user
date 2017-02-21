package com.miguo.category.dialog;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.UserInfoNew;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.dao.GetSMSCodeDao;
import com.miguo.dao.LoginByMobilleWithSMSDao;
import com.miguo.dao.impl.GetSMSCodeDaoImpl;
import com.miguo.dao.impl.LoginByMobilleWithSMSDaoImpl;
import com.miguo.dialog.HiBaseDialog;
import com.miguo.dialog.OfflinePayLoginDialog;
import com.miguo.listener.dialog.OfflinePayLoginDialogListener;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.view.GetSMSCodeView;
import com.miguo.view.LoginByMobilleWithSMSView;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/16.
 */

public class OfflinePayLoginDialogCategory extends DialogFragmentCategory {

    @ViewInject(R.id.content_layout)
    RelativeLayout content;

    @ViewInject(R.id.phone)
    EditText phone;

    @ViewInject(R.id.get_sms_code)
    TextView getSMSCode;

    @ViewInject(R.id.sms_code)
    EditText sms;

    @ViewInject(R.id.cancel)
    ImageView cancel;

    @ViewInject(R.id.sure)
    TextView sure;

    LoginByMobilleWithSMSDao loginByMobilleWithSMSDao;

    GetSMSCodeDao getSMSCodeDao;

    int preSMSBackground;

    TimerCount timerCount;

    public OfflinePayLoginDialogCategory(View view, HiBaseDialog fragment) {
        super(view, fragment);
    }

    @Override
    protected void findFragmentViews() {
        ViewUtils.inject(this, view);
    }

    @Override
    protected void initFirst() {
        initGetSMSCodeDao();
        initLoginByMobilleWithSMSDao();
    }

    @Override
    protected void initFragmentListener() {
        listener = new OfflinePayLoginDialogListener(this);
    }

    @Override
    protected void setFragmentListener() {
        getSMSCode.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
        sure.setOnClickListener(listener);
        sms.addTextChangedListener(listener);
    }

    @Override
    protected void init() {
        initContentPosition();
        initTimer();
    }

    private void initTimer(){
        timerCount = new TimerCount(60000, 1000);
    }

    /**
     * 验证码快速登录
     */
    private void initLoginByMobilleWithSMSDao(){
        loginByMobilleWithSMSDao = new LoginByMobilleWithSMSDaoImpl(new LoginByMobilleWithSMSView() {
            @Override
            public void loginByMobileWithSMSSuccess(UserInfoNew userInfoNew) {
                if(null != getDialog().getOnOfflinePayDialogListener()){
                    getDialog().getOnOfflinePayDialogListener().loginSuccess();
                }
                dismiss();
            }

            @Override
            public void loginByMobileWithSMSError(String message) {
                timerCount.onFinish();
                showToast(message);
            }
        });
    }

    /**
     * 获取短信验证码
     */
    private void initGetSMSCodeDao(){
        getSMSCodeDao = new GetSMSCodeDaoImpl(new GetSMSCodeView() {
            @Override
            public void getSMSCodeSuccess(String message) {
                showToast(message);
            }

            @Override
            public void getSMSCodeError(String message) {
                showToast(message);
            }
        });
    }

    private void initContentPosition(){
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(dip2px(273), dip2px(173));
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(0, (int)(getScreenHeight() * 0.20), 0, 0);
        content.setLayoutParams(params);
    }

    public void onTextChanged(){
        if(TextUtils.isEmpty(sms.getText().toString())){
            preSMSBackground = R.drawable.shape_bg_gray_f2_8;
            sure.setBackground(getDrawable(R.drawable.shape_bg_gray_f2_8));
            sure.setTextColor(getColor(R.color.c_999999));
            return;
        }
        if(preSMSBackground == R.drawable.shape_bg_gray_f2_8){
            sure.setBackground(getDrawable(R.drawable.shape_bg_yellow_8dp));
            sure.setTextColor(getColor(R.color.white));
            preSMSBackground = R.drawable.shape_bg_yellow_8dp;
        }
    }

    public void clickSMSCode(){
        String numberPhone = phone.getText().toString();
        if (TextUtils.isEmpty(numberPhone)) {
            MGToast.showToast("请输入手机号码!");
            return;
        }
        if (numberPhone.length() != 11) {
            MGToast.showToast("请输入正确的手机号码");
            return;
        }
        getSMSCodeDao.getSMSCodeForQuickLoginAndBindMobile(phone.getText().toString());
        timerCount.start();
    }

    public void clickSure(){
        if(TextUtils.isEmpty(sms.getText().toString())){
            showToast("请填写验证码！");
            return;
        }
        loginByMobilleWithSMSDao.loginByMobileWithSMS(phone.getText().toString(), sms.getText().toString(), "");
    }

    public void clickCancel(){
        dismiss();
    }

    public class TimerCount extends CountDownTimer {

        public TimerCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {
            getSMSCode.setText("获取验证码");
            getSMSCode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getSMSCode.setText(millisUntilFinished / 1000 + "s");
            getSMSCode.setEnabled(false);
        }
    }

    @Override
    public OfflinePayLoginDialog getDialog() {
        return (OfflinePayLoginDialog)super.getDialog();
    }
}
