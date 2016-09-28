package com.fanwe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fanwe.app.AppHelper;
import com.fanwe.base.CallbackView;
import com.fanwe.base.CallbackView2;
import com.fanwe.base.CommonHelper;
import com.fanwe.base.Root;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.dao.LocalUserModelDao;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.customview.SDSendValidateButton;
import com.fanwe.library.customview.SDSendValidateButton.SDSendValidateButtonListener;
import com.fanwe.library.utils.MD5Util;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.model.LocalUserModel;
import com.fanwe.network.MgCallback;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.postShopComment.RootShopComment;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.UserInfoNew;
import com.fanwe.user.presents.UserHttpHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;
import com.sunday.eventbus.SDBaseEvent;

import java.lang.reflect.Type;
import java.util.List;

public class ModifyPasswordActivity extends BaseActivity implements CallbackView, CallbackView2 {

    @ViewInject(R.id.svb_validate)
    private SDSendValidateButton mSvb_validate;

    @ViewInject(R.id.cet_mobile)
    private ClearEditText mCet_mobile;

    @ViewInject(R.id.cet_code)
    private ClearEditText mCet_code;

    @ViewInject(R.id.cet_pwd)
    private ClearEditText mCet_pwd;

    @ViewInject(R.id.cet_pwd_confirm)
    private ClearEditText mCet_pwd_confirm;

    @ViewInject(R.id.tv_sbumit)
    private TextView mTv_sbumit;

    private String mStrMobile;
    private String mStrCode;
    private String mStrPwd;
    private String mStrPwdConfirm;

    private String userPhone;
    private CommonHelper commonHelper;
    private UserHttpHelper userHttpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_modify_password);
        init();
    }

    private void init() {
        commonHelper = new CommonHelper(this, this);
        userHttpHelper = new UserHttpHelper(this, this);
        initTitle();
        registeClick();
        initSDSendValidateButton();
        showBindPhoneDialog();
        getIntentData();
    }

    private String pageType;

    private void getIntentData() {
        if (getIntent() != null) {
            pageType = getIntent().getStringExtra("pageType");
            if ("forget".equals(pageType)) {
                mTitle.setMiddleTextTop("忘记密码");
                mCet_mobile.setEnabled(true);
            }
        }
    }

    /**
     * 用JAVA 接口请求验证码。
     */
    private void doGetCaptcha() {
        userPhone = mCet_mobile.getText().toString();
        if (TextUtils.isEmpty(userPhone)) {
            MGToast.showToast("请输入手机号码");
            return;
        }
        //开始倒计时。
        mSvb_validate.setmDisableTime(60);
        mSvb_validate.startTickWork();

        commonHelper.doGetCaptcha(userPhone, 2, new MgCallback() {

            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast("验证码发送失败");
            }

            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<UserInfoNew>>() {
                }.getType();
                Gson gson = new Gson();
                Root root = gson.fromJson(responseBody, type);
                String status = root.getStatusCode();
                if (UserConstants.SUCCESS.equals(status)) {
                    MGToast.showToast("验证码发送成功");
                } else if (UserConstants.CODE_ERROR.equals(status)) {
                    MGToast.showToast("验证码发送失败");
                }
            }
        });
    }

    private void showBindPhoneDialog() {
        LocalUserModel user = AppHelper.getLocalUser();
        if (user != null) {
            mCet_mobile.setEnabled(false);
            String mobile = user.getUser_mobile();
            if (TextUtils.isEmpty(mobile)) {
                // TODO 跳到绑定手机号界面
            } else {
                mCet_mobile.setText(mobile);
            }
        } else {
            mCet_mobile.setEnabled(true);
        }
    }

    /**
     * 初始化发送验证码按钮
     */
    private void initSDSendValidateButton() {
        mSvb_validate.setmListener(new SDSendValidateButtonListener() {
            @Override
            public void onTick() {
            }

            @Override
            public void onClickSendValidateButton() {
                doGetCaptcha();
            }
        });
    }


    private void initTitle() {
        mTitle.setMiddleTextTop("修改密码");
    }

    private void registeClick() {
        mTv_sbumit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sbumit:
                clickSubmit();
                break;

            default:
                break;
        }
    }

    private void clickSubmit() {
        if (validateParam()) {
            if ("forget".equals(pageType)) {
                //忘记密码
                userHttpHelper.userForget(mStrMobile, MD5Util.MD5(mStrPwd), mStrCode);
            } else {
                //修改密码
                userHttpHelper.userChangePwd(mStrMobile, MD5Util.MD5(mStrPwd), mStrCode);
            }
        }
    }

    private boolean validateParam() {
        mStrMobile = mCet_mobile.getText().toString();
        if (TextUtils.isEmpty(mStrMobile)) {
            MGToast.showToast("手机号不能为空");
            return false;
        }

        mStrCode = mCet_code.getText().toString();
        if (TextUtils.isEmpty(mStrCode)) {
            MGToast.showToast("验证码不能为空");
            return false;
        }
        mStrPwd = mCet_pwd.getText().toString();
        if (TextUtils.isEmpty(mStrPwd)) {
            MGToast.showToast("新密码不能为空");
            return false;
        }
        if (mStrPwd.length() < 6) {
            MGToast.showToast("密码不能少于6位");
            return false;
        }
        if (mStrPwd.length() > 20) {
            MGToast.showToast("密码不能大于20位");
            return false;
        }
        mStrPwdConfirm = mCet_pwd_confirm.getText().toString();
        if (!mStrPwd.equals(mStrPwdConfirm)) {
            MGToast.showToast("两次密码不一致");
            return false;
        }

        return true;
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case CONFIRM_IMAGE_CODE:
                break;

            default:
                break;
        }
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    List<RootShopComment> roots;

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (UserConstants.USER_CHANGE_PWD.equals(method)) {
            roots = datas;
            if (!SDCollectionUtil.isEmpty(roots)) {
                RootShopComment root = roots.get(0);
                if (!"212".equals(root.getStatusCode())) {
                    //错误
                    MGToast.showToast(root.getMessage());
                    return;
                }
            }
            LocalUserModel localUserModel = LocalUserModelDao.queryModel();
            localUserModel.setUser_pwd(MD5Util.MD5(mStrPwd));
            LocalUserModelDao.insertModel(localUserModel);
            msg.what = 0;
        } else if (UserConstants.USER_FORGOT.equals(method)) {
            roots = datas;
            if (!SDCollectionUtil.isEmpty(roots)) {
                RootShopComment root = roots.get(0);
                if (!"212".equals(root.getStatusCode())) {
                    //错误
                    MGToast.showToast(root.getMessage());
                    return;
                }
            }
            msg.what = 1;
        }
        mHandler.sendMessage(msg);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    MGToast.showToast("密码修改成功");
                    finish();
                    break;
                case 1:
                    MGToast.showToast("密码修改成功");
                    finish();
                    break;
            }
        }
    };

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }
}