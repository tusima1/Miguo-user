package com.fanwe.user.presents;

import android.content.Context;
import android.util.Log;

import com.fanwe.base.Presenter;
import com.fanwe.fragment.LoginFragment;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;


import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 *
 * Created by Administrator on 2016/7/22.
 */
public class LoginHelper extends Presenter {
    private Context mContext;
    private static final String TAG = LoginHelper.class.getSimpleName();
    private LoginFragment mLoginView;
    private int RoomId = -1;

    public LoginHelper(Context context) {
        mContext = context;
    }

    public LoginHelper(Context context, LoginFragment loginView) {
        mContext = context;
        mLoginView = loginView;
    }


    /**
     * 登录
     * @param userName
     * @param password
     * @param  type 0为手机登录。1为第三方登录
     *
     */
    public void doLogin(String userName, String password, int  type) {
        TreeMap<String, String> params = new TreeMap<String,String>();
        params.put("mobile",userName);
        params.put("pwd",password);
        params.put("method", UserConstants.USER_lOGIN);
        OkHttpUtils.getInstance().get(null,params,new MgCallback(){

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.d("responseBody:",responseBody);
                mLoginView.loginSucc(responseBody);
            }
        });
    }

    /**
     * 注册 。
     * @param userName 手机号
     * @param captcha 验证码
     */
    public void doRegister(String userName, String captcha, String password) {
        TreeMap<String, String> params = new TreeMap<String,String>();
        params.put("mobile",userName);
        params.put("pwd",password);
        params.put("captcha",captcha);
        params.put("method", UserConstants.USER_REGISTER);
        OkHttpUtils.getInstance().post(null,params,new MgCallback(){

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.d("responseBody:",responseBody);
                mLoginView.loginSucc(responseBody);
            }
        });
    }

    /**
     * 退出
     */
    public void doLogout() {


    }






    @Override
    public void onDestory() {
        mLoginView = null;
        mContext = null;
    }
}
