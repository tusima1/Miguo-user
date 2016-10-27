package com.miguo.dao.impl;

import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.google.gson.Gson;
import com.miguo.dao.GetAdspaceListDao;
import com.miguo.entity.AdspaceListBean;
import com.miguo.entity.HomeGreetingBean;
import com.miguo.view.BaseView;
import com.miguo.view.GetAdspaceListView;

import java.util.TreeMap;

/**
 * Created by Administrator on 2016/10/27.
 */
public class GetAdspaceListDaoImpl extends BaseDaoImpl implements GetAdspaceListDao{

    public GetAdspaceListDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public GetAdspaceListView getListener() {
        return (GetAdspaceListView)baseView;
    }

    @Override
    public void getAdspaceList(String city_id) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("method", "GetCurGreeting");
        map.put("token", App.getApplication().getToken());
        OkHttpUtils.getInstance().get("", map, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                AdspaceListBean bean = new Gson().fromJson(responseBody, AdspaceListBean.class);
                if(bean != null){
                    if(bean.getStatusCode() == 200){
                        getListener().getAdspaceListSuccess(bean.getResult().get(0).getBody());
                    }else {
                        getListener().getAdspaceListError();
                    }
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                Log.d(tag, "onErrorResponse : " + message);
            }

        });
    }
}
