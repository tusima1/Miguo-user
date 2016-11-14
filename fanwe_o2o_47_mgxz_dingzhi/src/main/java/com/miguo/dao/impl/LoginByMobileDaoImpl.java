package com.miguo.dao.impl;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;
import com.miguo.dao.LoginByMobileDao;
import com.miguo.entity.LoginUserBean;
import com.miguo.view.BaseView;
import com.miguo.view.LoginByMobileView;

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
                if(userBean.getStatusCode() == 210){//Not 200
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
                    getListener().loginSuccess(userBean.getResult().get(0).getBody().get(0), mobile, password);
                }else {
                    /**
                     * 状态码不是200，登录失败
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
}
