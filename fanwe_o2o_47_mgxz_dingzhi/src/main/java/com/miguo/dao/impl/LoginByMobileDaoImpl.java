package com.miguo.dao.impl;

import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.UserInfoNew;
import com.miguo.dao.LoginByMobileDao;
import com.miguo.entity.LoginUserBean;
import com.miguo.utils.SharedPreferencesUtils;
import com.miguo.view.BaseView;
import com.miguo.view.LoginByMobileView;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;

import java.util.TreeMap;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/7.
 * 用户名密码登录接口实现类
 */
public class LoginByMobileDaoImpl extends BaseDaoImpl implements LoginByMobileDao{

    public LoginByMobileDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public LoginByMobileView getListener() {
        return (LoginByMobileView)baseView;
    }

    @Override
    public void loginByMobile(final String mobile, final String password) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("mobile", mobile);
        params.put("pwd", password);
        params.put("method", UserConstants.USER_lOGIN);
        OkHttpUtils.getInstance().get(null, params, new MgCallback(LoginUserBean.class) {


            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                LoginUserBean userBean = (LoginUserBean)responseBody;
                if(userBean.getStatusCode() == 210){
                    /**
                     * 如果Result实体为空
                     */
                    if(null == userBean.getResult() || null == userBean.getResult().get(0)){
                        getListener().loginError(userBean.getMessage());
                        return;
                    }

                    /**
                     * 如果Body实体为空
                     */
                    if(null == userBean.getResult().get(0).getBody() || null == userBean.getResult().get(0).getBody().get(0)){
                        getListener().loginError(userBean.getMessage());
                        return;
                    }

                    /**
                     * 登录成功
                     */
                    getListener().loginSuccess(userBean.getResult().get(0).getBody().get(0));
                    saveUserToLocal(userBean.getResult().get(0).getBody().get(0), mobile, password);
                    handleApplicationCurrentUser(userBean.getResult().get(0).getBody().get(0));
                    handleSaveUser(mobile, password);
                    initJpush();
                }else {
                    /**
                     * 状态码不是210，登录失败
                     */
                    getListener().loginError(userBean.getMessage());
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().loginError(message);
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

        if(!"".equals(mobile)){

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
        JpushHelper.initJPushConfig();
    }

}
