package com.miguo.dao.impl;

import com.fanwe.base.Root;
import com.fanwe.common.model.CommonConstants;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguo.dao.GetShareIdByCodeDao;
import com.miguo.utils.MGLog;
import com.miguo.view.BaseView;
import com.miguo.view.GetShareIdByCodeView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/11/30.
 */

public class GetShareIdByCodeDaoImpl extends BaseDaoImpl implements GetShareIdByCodeDao{

    public GetShareIdByCodeDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void getShareIdByCode(String code) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("receive_code", code);
        params.put("method", CommonConstants.GETSHAREID);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<HashMap<String, String>>>() {}.getType();
                Gson gson = new Gson();
                Root<HashMap<String, String>> root = gson.fromJson(responseBody, type);
                String status = root.getStatusCode();
                if ("200".equals(status)) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) validateBody(root);
                    if (hashMap != null && null != hashMap.get(CommonConstants.SHARE_RECORD_ID)) {
                        getListener().getShareIdSucess(hashMap.get(CommonConstants.SHARE_RECORD_ID));
                    } else {
                        getListener().getShareIdError(root.getMessage());
                    }
                } else {
                    getListener().getShareIdError(root.getMessage());
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGLog.e(CommonConstants.GETSHAREID + " :" + message + errorCode);
                getListener().getShareIdError(message);

            }
        });
    }

    @Override
    public GetShareIdByCodeView getListener() {
        return (GetShareIdByCodeView)baseView;
    }
}
