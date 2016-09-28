package com.fanwe.dao.barry.impl;

import com.fanwe.app.App;
import com.fanwe.dao.barry.UserAgreementDao;
import com.fanwe.dao.barry.view.UserAgreementView;
import com.fanwe.model.UserUpdateInfoBean;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.google.gson.Gson;
import com.miguo.live.model.LiveConstants;

import java.util.TreeMap;

/**
 * Created by Administrator on 2016/9/28.
 */
public class UserAgreementDaoImpl implements UserAgreementDao{

    UserAgreementView listener;

    public UserAgreementDaoImpl(UserAgreementView listener){
        this.listener = listener;
    }

    @Override
    public void getUserAgreement(String type) {
//        TreeMap<String, String> params = new TreeMap<>();
//        params.put("token", App.getInstance().getToken());
//        params.put("method", LiveConstants.USER_AGREEMENT);
//        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
//            @Override
//            public void onSuccessResponse(String responseBody) {
//                UserUpdateInfoBean bean = new Gson().fromJson(responseBody, UserUpdateInfoBean.class);
//                if(bean.getStatusCode() == 200){
//                    listener.getUserAgreementSuccess(bean.getResult().get(0).getBody().get(0));
//                }else {
//                    listener.getUserUpdateInfoError(bean.getMessage());
//                }
//            }
//
//            @Override
//            public void onErrorResponse(String message, String errorCode) {
//
//            }
//
//            @Override
//            public void onFinish() {
//            }
//        });
    }
}
