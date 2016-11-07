package com.miguo.view;

import com.fanwe.user.model.UserInfoNew;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/7.
 * 用户名密码登录回调接口
 */
public interface LoginByMobileView extends BaseView{
    void loginError(String message);
    void loginSuccess(UserInfoNew user, String mobile, String password);
}
