package com.miguo.dao.impl;

import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.base.Root;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.UserInfoNew;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguo.dao.LoginByMobilleWithSMSDao;
import com.miguo.utils.SharedPreferencesUtils;
import com.miguo.view.BaseView;
import com.miguo.view.LoginByMobilleWithSMSView;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;

import java.lang.reflect.Type;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/12/2.
 */

public class LoginByMobilleWithSMSDaoImpl extends BaseDaoImpl implements LoginByMobilleWithSMSDao{

    public LoginByMobilleWithSMSDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void loginByMobileWithSMS(final String mobile, String smsCode, String shareCode) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("mobile", mobile);
        params.put("captcha", smsCode);
        params.put("share_record_id",shareCode);
        params.put("method", UserConstants.USER_QUICK_LOGIN);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<UserInfoNew>>() {}.getType();
                Gson gson = new Gson();
                Root<UserInfoNew> root = gson.fromJson(responseBody, type);
                String status = root.getStatusCode();
                String message = root.getMessage();
                /**
                 * 登录成功的返回状态是210
                 */
                if(!status.equals("210")) {
                    getListener().loginByMobileWithSMSError(message);
                    return;
                }
                if (null == root.getResult() || null == root.getResult().get(0)) {
                    getListener().loginByMobileWithSMSError("登录失败！");
                    return;
                }

                if (null == root.getResult().get(0).getBody() || null == root.getResult().get(0).getBody().get(0)) {
                    getListener().loginByMobileWithSMSError("登录失败！");
                    return;
                }

                UserInfoNew userInfoNew = root.getResult().get(0).getBody().get(0);
                if (null == userInfoNew) {
                    getListener().loginByMobileWithSMSError("登录失败！");
                    return;
                }
                userInfoNew.setToken(root.getToken());
                /**
                 * 登录成功
                 */
                saveUserToLocal(userInfoNew, mobile, userInfoNew.getPwd());
                handleApplicationCurrentUser(userInfoNew);
                handleSaveUser(mobile, userInfoNew.getPwd());
                initJpush();
                getListener().loginByMobileWithSMSSuccess(userInfoNew);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().loginByMobileWithSMSError(message);
            }
        });
    }

    private void handleApplicationCurrentUser(UserInfoNew userInfoNew){
        if (userInfoNew != null) {
            App.getInstance().setCurrentUser(userInfoNew);
        }
    }

    /**
     * 将用户信息保存到本地以及全局
     *
     * @param user
     */
    private void saveUserToLocal(UserInfoNew user, String mobile, String password) {
        UserInfoNew userInfoNew = user;
        if (userInfoNew != null) {
            App.getInstance().setCurrentUser(userInfoNew);
            User_infoModel model = new User_infoModel();
            model.setUser_id(userInfoNew.getUser_id());
            MySelfInfo.getInstance().setId(userInfoNew.getUser_id());
            if (!TextUtils.isEmpty(mobile)) {
                model.setMobile(mobile);
            }
            if (!TextUtils.isEmpty(password)) {
                model.setUser_pwd(password);
            }
            if (!TextUtils.isEmpty(userInfoNew.getPwd())) {
                model.setUser_pwd(userInfoNew.getPwd());
            }
            model.setUser_name(userInfoNew.getUser_name());

            LocalUserModel.dealLoginSuccess(model, true);
        }
    }

    /**
     * 保存用户信息SharedPreferences
     *
     * @param mobile
     * @param password
     */
    private void handleSaveUser(String mobile, String password) {
        SharedPreferencesUtils.getInstance().saveUserNameAndUserPassword(mobile, password);
    }

    /**
     * 推送
     */
    private void initJpush() {
        JpushHelper.registerAll();
    }

    @Override
    public LoginByMobilleWithSMSView getListener() {
        return (LoginByMobilleWithSMSView)baseView;
    }
}
