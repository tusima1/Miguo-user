package com.fanwe.user.presents;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.fanwe.base.Presenter;
import com.fanwe.base.Result;
import com.fanwe.fragment.LoginFragment;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;

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
        OkHttpUtils.getInstance().post(null,params,new MgCallback<JSONObject>(){

            @Override
            public void onSuccessResponse(Result<JSONObject> responseBody) {
                if(responseBody==null ||responseBody.getBody()==null){
                    onErrorResponse("登录失败",null);
                }
                if(responseBody.getBody().size()>0) {


                    mLoginView.loginSucc(responseBody.getBody().get(0));
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
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
        OkHttpUtils.getInstance().post(null,params,new MgCallback<JSONObject>(){

            @Override
            public void onSuccessResponse(Result<JSONObject> responseBody) {
               if(responseBody==null ||responseBody.getBody()==null){
                   onErrorResponse("注册失败",null);
               }
                if(responseBody.getBody().size()>0) {

                    mLoginView.loginSucc(responseBody.getBody().get(0));
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });
    }

    /**
     * 验证手机号是否已经存在。
     * @param mobile
     */
    public void doCheckMobileExist(String mobile){

        TreeMap<String, String> params = new TreeMap<String,String>();
        params.put("mobile",mobile);
        params.put("method", UserConstants.USER_CHECK_EXIST);
        OkHttpUtils.getInstance().get(null,params,new MgCallback<JSONObject>(){

            @Override
            public void onSuccessResponse(Result<JSONObject> responseBody) {
                if(responseBody==null ||responseBody.getBody()==null){
                    onErrorResponse("验证手机号失败",null);
                }
                if(responseBody.getBody().size()>0) {

                    mLoginView.loginSucc(responseBody.getBody().get(0));
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
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
