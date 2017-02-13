package com.fanwe.dao.barry.impl;

import com.fanwe.app.App;
import com.fanwe.dao.barry.UserUpdateDao;
import com.fanwe.dao.barry.view.UserUpdateView;
import com.fanwe.model.UserUpdateInfoBean;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.google.gson.Gson;
import com.miguo.live.model.LiveConstants;

import java.util.TreeMap;

/**
 * Created by Administrator on 2016/9/28.
 */
public class UserUpdateInfoDaoImpl implements UserUpdateDao {

    UserUpdateView listener;


    public UserUpdateInfoDaoImpl(UserUpdateView listener){
        this.listener = listener;
    }

    @Override
    public void getUserUpdateInfo(String token) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("token", App.getInstance().getToken());
        params.put("method", LiveConstants.USER_UPGRADE_ORDER);
        OkHttpUtil.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                UserUpdateInfoBean bean = new Gson().fromJson(responseBody, UserUpdateInfoBean.class);
                if(bean.getStatusCode() == 200){
                    listener.getUserUpdateInfoSuccess(bean.getResult().get(0).getBody().get(0));
                }else {
                    listener.getUserUpdateInfoError(bean.getMessage());
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {

            }

            @Override
            public void onFinish() {
            }
        });
    }

    @Override
    public void updateUserLevel(String payment_id) {

    }
}
