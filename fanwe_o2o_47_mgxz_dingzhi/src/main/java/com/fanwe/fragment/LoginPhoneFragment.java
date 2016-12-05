package com.fanwe.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fanwe.base.CallbackView;
import com.fanwe.base.CommonHelper;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.model.Check_MobActModel;
import com.fanwe.network.MgCallback;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.presents.LoginHelper;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiLoginActivity;
import com.miguo.live.views.customviews.MGToast;
import com.sunday.eventbus.SDBaseEvent;

import java.util.List;

/**
 * 快速登录
 */
public class LoginPhoneFragment extends LoginBaseFragment implements CallbackView {

    public static final String TAG = LoginPhoneFragment.class.getSimpleName();
    public static final String EXTRA_PHONE_NUMBER = "extra_phone_number";

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

    CommonHelper mFragmentHelper;
    private TimeCount time;
    LoginHelper mLoginHelper;

    /**
     * 分享ID。
     */
    String shareCode="";

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_phone_login);
    }

    @Override
    protected void init() {
        super.init();
        mFragmentHelper = new CommonHelper(getActivity(), this);
        mLoginHelper = new LoginHelper(getActivity(), getActivity(), null);
        getIntentData();

        registeClick();
        mBtnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMobileExist();
            }
        });
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
    }

    /**
     * 判断手机号是否存在。
     */
    public void checkMobileExist() {
        mNumberPhone = mEtMobile.getText().toString();
        if (TextUtils.isEmpty(mNumberPhone)) {
            MGToast.showToast("请输入手机号码");
            return;
        }
        if (mNumberPhone.length() != 11) {
            MGToast.showToast("请输入正确的手机号码");
            return;
        }
        mFragmentHelper.doCheckMobileExist(mNumberPhone, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                time.start();//开始计时
                requestCaptcha();

            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 手机验证码发送。
     */
    public void requestCaptcha() {
        mNumberPhone = mEtMobile.getText().toString();
        if (TextUtils.isEmpty(mNumberPhone)) {
            MGToast.showToast("请输入手机号码");
            return;
        }
        if (mNumberPhone.length() != 11) {
            MGToast.showToast("请输入正确的手机号码");
            return;
        }
        mFragmentHelper.doGetCaptcha(mNumberPhone, 4, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast("验证码发送失败，请重新发送");
                mBtnSendCode.setText("重新发送验证码");
                time.onFinish();
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                MGToast.showToast("验证码发送成功");
            }
        });
    }

    protected void getIntentData() {
        String mobile = getActivity().getIntent().getStringExtra(EXTRA_PHONE_NUMBER);

        if (!TextUtils.isEmpty(mobile)) {
            mEtMobile.setText(mobile);
        }
    }


    private void registeClick() {
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                quickLogin();
                break;

            default:
                break;
        }
    }


    /**
     * 快捷 登录 接口。
     */
    private void quickLogin() {
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
        shareCode = ((HiLoginActivity)getActivity()).getCategory().getShareCode();
        mLoginHelper.doQuickLogin(mNumberPhone, mStrCode,mBtnLogin,shareCode);
        SDDialogManager.showProgressDialog("请稍候...");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);

    }


    @Override
    public void onSuccess(String responseBody) {
        MGToast.showToast("验证码发送成功");
    }

    @Override
    public void onSuccess(String method, List datas) {

    }

    @Override
    public void onFailue(String responseBody) {

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


}