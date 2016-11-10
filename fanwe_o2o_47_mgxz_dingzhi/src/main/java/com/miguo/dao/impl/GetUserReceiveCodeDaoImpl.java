package com.miguo.dao.impl;

import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.miguo.dao.GetUserReceiveCodeDao;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.getUseReceiveCode.ResultUseReceiveCode;
import com.miguo.live.model.getUseReceiveCode.RootUseReceiveCode;
import com.miguo.view.BaseView;
import com.miguo.view.GetUserReceiveCodeView;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/7.
 * 使用分享领取码进入房间并领钻 接口实现类
 */
public class GetUserReceiveCodeDaoImpl extends BaseDaoImpl implements GetUserReceiveCodeDao {

    public GetUserReceiveCodeDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public GetUserReceiveCodeView getListener() {
        return (GetUserReceiveCodeView) baseView;
    }

    @Override
    public void getUserReceiveCode(String receive_code) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        if (!TextUtils.isEmpty(receive_code)) {
            receive_code = receive_code.trim();
        }
        params.put("receive_code", receive_code);

        params.put("method", LiveConstants.USE_RECEIVE_CODE);
        OkHttpUtils.getInstance().get(null, params, new MgCallback(RootUseReceiveCode.class) {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().getUserReceiveCodeError(message);
            }

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                RootUseReceiveCode receiveCode = (RootUseReceiveCode) responseBody;
                /**
                 * 请求成功
                 */
                if (receiveCode.getStatusCode().equals("200")) {
                    List<ResultUseReceiveCode> results = receiveCode.getResult();
                    if (null == results || null == results.get(0)) {
                        getListener().getUserReceiveCodeError(receiveCode.getMessage());
                        return;
                    }

                    if (null == results.get(0).getBody() || null == results.get(0).getBody().get(0)) {
                        getListener().getUserReceiveCodeError(receiveCode.getMessage());
                        return;
                    }

                    getListener().getUserReceiveCodeSuccess(results.get(0).getBody().get(0));
                    return;
                } else {
                    getListener().getUserReceiveCodeSuccess(null);
                }

                getListener().getUserReceiveCodeError(receiveCode.getMessage());

            }
        });
    }
}
