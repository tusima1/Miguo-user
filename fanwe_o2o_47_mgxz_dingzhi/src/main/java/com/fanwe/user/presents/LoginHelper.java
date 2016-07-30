package com.fanwe.user.presents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.baidu.mapapi.map.Text;
import com.fanwe.MainActivity;
import com.fanwe.app.App;
import com.fanwe.base.Presenter;
import com.fanwe.base.Result;
import com.fanwe.base.Root;
import com.fanwe.fragment.LoginFragment;
import com.fanwe.home.model.Room;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.utils.MD5Util;
import com.fanwe.library.utils.SDToast;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.UserInfoNew;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.TreeMap;


/**
 * Created by Administrator on 2016/7/22.
 */
public class LoginHelper extends Presenter {
    private Context mContext;
    private static final String TAG = LoginHelper.class.getSimpleName();
    private LoginFragment mLoginView;
    private int RoomId = -1;
    private Activity mActivity;

    public LoginHelper(Context context) {
        mContext = context;
    }
    public LoginHelper(Activity activity,Context context, LoginFragment loginView) {
        this.mActivity = activity;
        mContext = context;
        mLoginView = loginView;
    }
    public LoginHelper(Context context, LoginFragment loginView) {
        mContext = context;
        mLoginView = loginView;
    }

    /**
     * 快捷登录。
     * @param mobile
     * @param captcha
     */
    public void doQuickLogin(final String mobile,String captcha){
        TreeMap<String, String> params = new TreeMap<String,String>();
        params.put("mobile", mobile);
        params.put("captcha",captcha);
        params.put("method", UserConstants.USER_QUICK_LOGIN);
        OkHttpUtils.getInstance().post(null,params,new MgCallback(){

            @Override
            public void onSuccessResponse(String responseBody) {
                dealLoginInfo(responseBody,mobile,null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {

                SDToast.showToast(message);
            }
        });

    }
    /**
     * 登录
     *
     * @param userName
     * @param password
     * @param type     0为手机登录。1为第三方登录
     */
    public void doLogin(final String userName, final String password, int type) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("mobile", userName);
        params.put("pwd", password);
        params.put("method", UserConstants.USER_lOGIN);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {


            @Override
            public void onSuccessResponse(String responseBody)  {
                dealLoginInfo(responseBody,userName,password);
            }


            @Override

            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }



    /**
     * 注册 。
     */
    public void doRegister(final String userName, String captcha, final String password) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("mobile", userName);
        params.put("pwd", MD5Util.MD5(password));
        params.put("captcha", captcha);
        params.put("method", UserConstants.USER_REGISTER);
        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                dealLoginInfo(responseBody,userName,password);
            }
            @Override
            public void onErrorResponse(String message, String errorCode) {

                SDToast.showToast(message);
            }
        });
    }
    /**
     * 第三方注册 。
     */
    public void doThirdRegister(final  String userPhone,String openid,String captcha,String icon,String nick,String platform){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("mobile", userPhone);
        params.put("openid", openid);
        params.put("captcha", captcha);
        params.put("icon",icon);
        params.put("nick",nick);
        params.put("platform",platform);
        params.put("method", UserConstants.THIRD_REGISTER_URL);
        OkHttpUtils.getInstance().post(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                dealLoginInfo(responseBody,userPhone,null);
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


    public void dealLoginInfo(String responseBody,String userName,String password){
        Type type = new TypeToken<Root<UserInfoNew>>() {
        }.getType();
        Gson gson = new Gson();
        Root<UserInfoNew> root = gson.fromJson(responseBody, type);
        UserInfoNew userInfoNew = (UserInfoNew) validateBody(root);
        if (userInfoNew != null) {
            if (userInfoNew != null) {
                App.getInstance().getmUserCurrentInfo().setUserInfoNew(userInfoNew);
                User_infoModel model = new User_infoModel();
                model.setUser_id(userInfoNew.getUser_id());
                if(!TextUtils.isEmpty(userName)) {
                    model.setMobile(userName);
                }
                if(!TextUtils.isEmpty(password)) {
                    model.setUser_pwd(password);

                }
                model.setUser_name(userInfoNew.getUser_name());
                dealLoginSuccess(model);
            }
        }
    }
    /**
     * 判断BODY对象是否存在。
     * @param root
     * @return
     */
    public  UserInfoNew validateBody(Root<UserInfoNew> root) {

        if (root.getResult() != null && root.getResult().size() > 0 && root.getResult().get(0) != null && root.getResult().get(0).getBody() != null && root.getResult().get(0).getBody().size() > 0)
        {
            return root.getResult().get(0).getBody().get(0);
        }
        return null;

    }
    protected void dealLoginSuccess(User_infoModel actModel) {
        LocalUserModel.dealLoginSuccess(actModel, true);
        Activity lastActivity = SDActivityManager.getInstance().getLastActivity();
        if (lastActivity instanceof MainActivity) {
            mActivity.finish();
        } else {
            mActivity. startActivity(new Intent(mActivity, MainActivity.class));
        }

    }
    @Override
    public void onDestory() {
        mLoginView = null;
        mContext = null;
        mActivity = null;
    }
}
