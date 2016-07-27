package com.fanwe.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.MainActivity;
import com.fanwe.app.AppConfig;
import com.fanwe.app.AppHelper;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDToast;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.presents.LoginHelper;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import java.util.Map;

public class LoginFragment extends LoginBaseFragment {

    @ViewInject(R.id.act_login_et_email)
    private ClearEditText mEtEmail;

    @ViewInject(R.id.act_login_et_pwd)
    private ClearEditText mEtPwd;

    @ViewInject(R.id.act_login_tv_login)
    private TextView mTvLogin;
    /**
     * qqq登录。
     */
    @ViewInject(R.id.qq_login)
    private Button qq_login;
    /**
     * 微博登录
     */

    @ViewInject(R.id.weibo_login)
    private Button weibo_login;
    /**
     * 微信登录
     */

    @ViewInject(R.id.weixin_login)
    private Button weixin_login;

    private String mStrUserName;
    private String mStrPassword;
    private UMShareAPI mShareAPI = null;
    LoginHelper mLoginHelper;


    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_login);
    }

    @Override
    protected void init() {
        super.init();
        registeClick();
        mLoginHelper = new LoginHelper(getActivity(), this);


        //友盟授权登录初始化。
        mShareAPI = UMShareAPI.get(getActivity());

    }


    private void validatePassword() {
        LocalUserModel user = AppHelper.getLocalUser();
        if (user != null) {
            String userName = user.getUser_name();
            if (!TextUtils.isEmpty(userName)) {
                mEtEmail.setText(userName);
                mEtEmail.setEnabled(false);
                mEtPwd.setHint("为了保证账户安全，请重新验证密码");
            }
        }
    }

    private void registeClick() {
        mTvLogin.setOnClickListener(this);
        qq_login.setOnClickListener(this);
        weibo_login.setOnClickListener(this);
        weixin_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        SHARE_MEDIA platform = null;
        switch (v.getId()) {
            case R.id.act_login_tv_login:
                doLogin();
               // clickLoginNormal();
                break;
            case R.id.qq_login:
                platform = SHARE_MEDIA.QQ;
                goToAuth(platform);
                break;
            case R.id.weibo_login:
                platform = SHARE_MEDIA.SINA;
                goToAuth(platform);
                break;
            case R.id.weixin_login:
                platform = SHARE_MEDIA.WEIXIN;
                goToAuth(platform);
                break;

            default:
                break;
        }
    }

    /**
     * 跳转到相应的授权页。
     *
     * @param platform
     */
    public void goToAuth(SHARE_MEDIA platform) {

        mShareAPI.doOauthVerify(getActivity(), platform, umAuthListener);
    }

    /**
     * 点击新浪微博登录,先获取个人资料，然后登录
     */
    private void clickLoginSina() {

    }

    protected void requestSinaLogin(String uid, String access_token, String nickname) {
        RequestModel model = new RequestModel();
        model.putCtl("synclogin");
        model.put("login_type", "Sina");
        model.put("sina_id", uid);
        model.put("access_token", access_token);
        model.put("nickname", nickname);
        SDRequestCallBack<User_infoModel> handler = new SDRequestCallBack<User_infoModel>() {

            @Override
            public void onStart() {
                SDDialogManager.showProgressDialog("请稍候...");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (actModel.getStatus() == 1) {
                    dealLoginSuccess(actModel);
                }
            }

            @Override
            public void onFinish() {
                SDDialogManager.dismissProgressDialog();
            }
        };
        InterfaceServer.getInstance().requestInterface(model, handler);

    }

    /**
     * auth callback interface
     **/
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            Log.d("user info", "user info:" + data.toString());
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getActivity().getApplicationContext(), "授权登录失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getActivity().getApplicationContext(), "授权登录取消", Toast.LENGTH_SHORT).show();
        }
    };




    protected void requestQQLogin(String openId, String access_token, String nickname) {
        RequestModel model = new RequestModel();
        model.putCtl("synclogin");
        model.put("login_type", "Qq");
        model.put("qqv2_id", openId);
        model.put("access_token", access_token);
        model.put("nickname", nickname);
        SDRequestCallBack<User_infoModel> handler = new SDRequestCallBack<User_infoModel>() {

            @Override
            public void onStart() {
                SDDialogManager.showProgressDialog("请稍候...");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (actModel.getStatus() == 1) {
                    dealLoginSuccess(actModel);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }

            @Override
            public void onFinish() {
                SDDialogManager.dismissProgressDialog();
            }
        };
        InterfaceServer.getInstance().requestInterface(model, handler);
    }

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
            mLoginHelper.doLogin(mStrUserName, mStrPassword, 0);
        }
    }
    private void clickLoginNormal() {
        if (validateParam()) {
            RequestModel model = new RequestModel();
            model.putCtl("user");
            model.putAct("dologin");
            model.put("user_key", mStrUserName);
            model.put("user_pwd", mStrPassword);
            SDRequestCallBack<User_infoModel> handler = new SDRequestCallBack<User_infoModel>() {
                @Override
                public void onStart() {
                    SDDialogManager.showProgressDialog("请稍候...");
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    SDDialogManager.dismissProgressDialog();
                    if (actModel.getStatus() == 1) {
                        AppConfig.setUserName(actModel.getUser_name());
                        dealLoginSuccess(actModel);
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    SDDialogManager.dismissProgressDialog();
                }

                @Override
                public void onFinish() {
                }
            };
            InterfaceServer.getInstance().requestInterface(model, handler);
        }
    }

    protected void dealLoginSuccess(User_infoModel actModel) {
        LocalUserModel.dealLoginSuccess(actModel, true);
        Activity lastActivity = SDActivityManager.getInstance().getLastActivity();
        if (lastActivity instanceof MainActivity) {
            getActivity().finish();
        } else {
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        }

    }

    private boolean validateParam() {
        mStrUserName = mEtEmail.getText().toString();
        if (TextUtils.isEmpty(mStrUserName)) {
            SDToast.showToast("请输入账号");
            mEtEmail.requestFocus();
            return false;
        }
        mStrPassword = mEtPwd.getText().toString();
        if (TextUtils.isEmpty(mStrPassword)) {
            SDToast.showToast("密码不能为空");
            mEtPwd.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * JAVA 登录接口。
     */
    public void   loginSucc(String responseBody){

    }
}