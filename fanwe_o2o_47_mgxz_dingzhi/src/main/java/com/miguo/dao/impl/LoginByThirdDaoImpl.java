package com.miguo.dao.impl;

import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.base.Root;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.ThirdLoginInfo;
import com.fanwe.user.model.UserInfoNew;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguo.dao.LoginByThirdDao;
import com.miguo.utils.SharedPreferencesUtils;
import com.miguo.view.BaseView;
import com.miguo.view.LoginByThirdView;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;

import java.lang.reflect.Type;
import java.util.TreeMap;

/**
 * Created by zlh on 2016/11/30.
 */

public class LoginByThirdDaoImpl extends BaseDaoImpl implements LoginByThirdDao{

    public LoginByThirdDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void thirdLogin(final String openId, final String platformType, final String icon, final String nick) {
        if (TextUtils.isEmpty(openId)) {
            return;
        }
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("openid", openId);
        params.put("platform", platformType);
        if (!TextUtils.isEmpty(icon)) {
            params.put("icon", icon);
        }
        params.put("nick", nick);

        params.put("method", UserConstants.TRHID_LOGIN_URL);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {

            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<UserInfoNew>>() {
                }.getType();
                Gson gson = new Gson();
                Root<UserInfoNew> root = gson.fromJson(responseBody, type);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                /**
                 * 登录成功
                 */
                if ("210".equals(statusCode)) {
                    UserInfoNew userInfoNew = (UserInfoNew) validateBody(root);
                    if(userInfoNew == null){
                        getListener().thirdLoginError("登录失败！");
                        return;
                    }
                    App.getInstance().setCurrentUser(userInfoNew);
                    User_infoModel model = new User_infoModel();
                    model.setUser_id(userInfoNew.getUser_id());
                    model.setMobile(userInfoNew.getMobile());
                    model.setUser_name(userInfoNew.getUser_name());
                    model.setUser_pwd(userInfoNew.getPwd());
                    saveUserToLocal(userInfoNew, userInfoNew.getMobile(), userInfoNew.getPwd());
                    userInfoNew.setToken(root.getToken());
                    handleApplicationCurrentUser(userInfoNew);
                    handleSaveUser(userInfoNew.getMobile(), userInfoNew.getPwd());
                    initJpush();
                    getListener().thirdLoginSuccess(model, userInfoNew);
                } else if ("300".equals(statusCode)) {
                    ThirdLoginInfo thirdLoginInfo = new ThirdLoginInfo();
                    thirdLoginInfo.setIcon(icon);
                    thirdLoginInfo.setNick(nick);
                    thirdLoginInfo.setOpenId(openId);
                    thirdLoginInfo.setPlatformType(platformType);
                    getListener().thirdLoginUnRegister(thirdLoginInfo);
                } else {
                    getListener().thirdLoginError(message);
                }

            }

            @Override
            public void onFinish() {
                SDDialogManager.dismissProgressDialog();
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
    public LoginByThirdView getListener() {
        return (LoginByThirdView)baseView;
    }
}
