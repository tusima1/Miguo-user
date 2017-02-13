package com.fanwe.dao.barry.impl;

import com.fanwe.app.App;
import com.fanwe.dao.barry.UserAgreementDao;
import com.fanwe.dao.barry.view.UserAgreementView;
import com.fanwe.model.UserAgreementBean;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
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
        TreeMap<String, String> params = new TreeMap<>();
        params.put("token", App.getInstance().getToken());
        params.put("type", type);
        params.put("method", LiveConstants.USER_AGREEMENT);
        OkHttpUtil.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                UserAgreementBean bean = new Gson().fromJson(responseBody, UserAgreementBean.class);
                if(bean.getStatusCode() == 200){
                    String html = bean.getResult().get(0).getBody().get(0).getHtml();
                    html = html.replaceAll("<\\/", "</");
                    html = html.replaceAll("u201c", "\"");
                    html = html.replaceAll("u201d", "\"");
                    html = html.replaceAll("\\\\", "");
                    html = html.replaceAll("/r/n", "");
                    html = html.replaceAll("\r\n", "");
                    listener.getUserAgreementSuccess(html);
                }else {
                    listener.getUserAgreementError(bean.getMessage());
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
}
