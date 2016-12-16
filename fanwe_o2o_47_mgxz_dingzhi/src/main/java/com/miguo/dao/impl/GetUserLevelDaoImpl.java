package com.miguo.dao.impl;

import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.base.Root;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguo.dao.GetUserLevelDao;
import com.miguo.view.BaseView;
import com.miguo.view.GetUserLevelView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by zlh on 2016/12/13.
 * 获取会员等级
 */
public class GetUserLevelDaoImpl extends BaseDaoImpl implements GetUserLevelDao {

    public GetUserLevelDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void getUserLevel() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.USER_DISTRIBUTION_LEVEL);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<HashMap<String, String>>>() {}.getType();
                Gson gson = new Gson();
                Root<HashMap<String, String>> root = gson.fromJson(responseBody, type);
                String status = root.getStatusCode();
                if (!"200".equals(status)) {
                    getListener().getUserLevelError("");
                    return;
                }
                List<HashMap<String, String>> datas = validateBodyList(root);
                if(null == datas || null == datas.get(0)){
                    getListener().getUserLevelError("");
                    return;
                }
                getListener().getUserLevelSuccess(getUserLevel(datas.get(0).get("fx_level")));
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().getUserLevelError(message);
            }
        });
    }

    private String getUserLevel(String level){
        return TextUtils.isEmpty(level) ? "1" : level;
    }

    @Override
    public GetUserLevelView getListener() {
        return (GetUserLevelView)baseView;
    }
}
