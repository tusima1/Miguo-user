package com.fanwe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.base.CommonHelper;
import com.fanwe.base.Root;
import com.fanwe.common.CommonInterface;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.fragment.LoginPhoneFragment;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.MD5Util;
import com.fanwe.library.utils.SDToast;
import com.fanwe.model.Check_MobActModel;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.Sms_send_sms_codeActModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.UserInfoNew;
import com.fanwe.user.presents.LoginHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * 注册界面
 *
 * @author Administrator
 */
public class RegisterActivity extends BaseActivity implements CallbackView {

    private static String TAG = RegisterActivity.class.getSimpleName();
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
    private String mStrPwd;

    private String userPhone;
    /**
     * 密码。
     */
    private String passwordStr;
    protected Check_MobActModel mActModel;
    CommonHelper mFragmentHelper;
    LoginHelper mLoginHelper;
    private TimeCount time;
    /**
     * 第三方OPENID ，用于第三方注册 。
     */
    String openid = "";
    String platform="";
    String icon = "";
    String nick="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_register);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(openid)){
            passline1.setVisibility(View.INVISIBLE);
            passline2.setVisibility(View.INVISIBLE);
        }
    }

    private void init() {
        mFragmentHelper = new CommonHelper(this, this);
        mLoginHelper = new LoginHelper(RegisterActivity.this,this,null);
        initGetIntent();
        initTitle();
        registeClick();
        checkMobileExist();
        initSDSendValidateButton();
    }

    private void initGetIntent() {
        Intent intent = getIntent();
        String phone = intent.getStringExtra(EXTRAS_Phone);
        if (!TextUtils.isEmpty(phone)) {
            mEtUserphone.setText(phone);
        }

        if (intent.hasExtra(UserConstants.THIRD_OPENID)) {
            openid = intent.getStringExtra(UserConstants.THIRD_OPENID);
            platform = intent.getStringExtra(UserConstants.THIRD_PLATFORM);
            icon = intent.getStringExtra(UserConstants.THIRD_ICON);
            nick = intent.getStringExtra(UserConstants.THIRD_NICK);
        }
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("注册");
    }

    private void registeClick() {
        mTvRegister.setOnClickListener(this);
        mLl_xieyi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                clickRegister();
                break;
            case R.id.ll_register_xieyi:
                clickXieyi();
                break;
            default:
                break;
        }
    }

    private void clickXieyi() {
        Intent intent = new Intent(this, RegisterAgreementActivity.class);
        startActivity(intent);
    }


    private void clickRegister() {
        if (validateParam()) {
            if(!TextUtils.isEmpty(openid)){
                mLoginHelper.doThirdRegister(userPhone,openid,mStrPwd, icon, nick,platform);
            }else {
               mLoginHelper.doRegister(userPhone,mStrPwd,passwordStr);
            }

        }
    }

    /**
     *  判断手机号是否存在。
     */
    public void checkMobileExist() {
        userPhone = mEtUserphone.getText().toString();
        if (TextUtils.isEmpty(userPhone)) {
            SDToast.showToast("请输入手机号码");
            return;
        }
        mFragmentHelper.doCheckMobileExist(userPhone, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<HashMap<String,String>>>() {
                }.getType();
                Gson gson = new Gson();
                Root<UserInfoNew> root = gson.fromJson(responseBody, type);
                HashMap<String,String> infoNew = (HashMap<String,String>) validateBody(root);
                if(infoNew!=null) {
                  String value =   infoNew.get("exist");
                    //如果手机号存在，并且是第三方登录。直接取验证码。
                    if("1".equals(value)){
                        if(!TextUtils.isEmpty(openid)) {

                            time.start();
                            doGetCaptcha();
                        }else{
                            goLogin();
                        }
                    }else{
                        time.start();
                        doGetCaptcha();
                    }
                }
            }
            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });
    }
    public void goLogin(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.putExtra(LoginActivity.EXTRA_SELECT_TAG_INDEX, 1);
        intent.putExtra(LoginPhoneFragment.EXTRA_PHONE_NUMBER, userPhone);
        startActivity(intent);
    }



    private void initSDSendValidateButton() {

        mBt_send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMobileExist();
            }
        });
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
    }

    /**
     * 用JAVA 接口请求验证码。
     */
    private void doGetCaptcha() {
        userPhone = mEtUserphone.getText().toString();
        if (TextUtils.isEmpty(userPhone)) {
            SDToast.showToast("请输入手机号码");
            return;
        }
        //开始倒计时。

        mFragmentHelper.doGetCaptcha(userPhone, 1, new MgCallback() {

            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast("验证码发送失败");
            }
            public void onSuccessResponse(String responseBody){
                Type type = new TypeToken<Root<UserInfoNew>>() {
                }.getType();
                Gson gson = new Gson();
                Root root = gson.fromJson(responseBody, type);
                  String status = root.getStatusCode();
                if(UserConstants.SUCCESS.equals(status)) {
                    SDToast.showToast("验证码发送成功");
                }else if(UserConstants.CODE_ERROR.equals(status)) {
                    SDToast.showToast("验证码发送失败");
                }
            }
        });
    }

    /**
     * 参数有效性判断。
     *
     * @return
     */
    private boolean validateParam() {
        userPhone = mEtUserphone.getText().toString();
        if (TextUtils.isEmpty(userPhone)) {
            SDToast.showToast("手机号不能为空");
            return false;
        }
        mStrPwd = mEtPwd.getText().toString();
        if (TextUtils.isEmpty(mStrPwd)) {
            SDToast.showToast("验证码不能为空");
            return false;
        }
        if (mCh_register.isChecked()) {
            SDToast.showToast("请先同意用户注册协议");
            return false;
        }
        String pwd2 = pwd.getText().toString();
        String pwd1 = mEt_pwd_into.getText().toString();
        if (!pwd2.equals(pwd1)) {
            SDToast.showToast("两次密码输入不一致。");
        }
        passwordStr = pwd1;
        return true;
    }


    @Override
    public void onSuccess(String responseBody) {

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
            mBt_send_code.setText("重新验证");
            mBt_send_code.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mBt_send_code.setClickable(false);
            mBt_send_code.setText(millisUntilFinished / 1000 + "秒");
        }
    }

}