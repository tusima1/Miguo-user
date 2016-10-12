package com.fanwe.dao.barry.impl;

import com.fanwe.app.App;
import com.fanwe.dao.barry.GetSpecialListDao;
import com.fanwe.dao.barry.view.GetSpecialListView;
import com.fanwe.model.SpecialListModel;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.google.gson.Gson;
import com.miguo.live.model.LiveConstants;

import java.util.TreeMap;

/**
 * Created by Administrator on 2016/10/11.
 */
public class GetSpecialListDaoImpl implements GetSpecialListDao{

    GetSpecialListView listener;

    public GetSpecialListDaoImpl(GetSpecialListView listener) {
        this.listener = listener;
    }

    @Override
    public void getSpecialList(String city_id, String cur_geo_x, String cur_geo_y, final String page) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("token", App.getInstance().getToken());
        params.put("method", LiveConstants.SPECIAL_LIST);
//        params.put("city_id", city_id);
        params.put("city_id", "e1b2911e-3a23-4630-9213-d317d200d9dc");
        params.put("cur_geo_x", cur_geo_x);
        params.put("cur_geo_y", cur_geo_y);
        params.put("page", page);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                SpecialListModel bean = new Gson().fromJson(responseBody, SpecialListModel.class);
                if(bean.getStatusCode() == 200){
                    if(page.equals("0") || page.equals("1")){
                        listener.getSpecialListSuccess(bean.getResult().get(0));
                    }else {
                        listener.getSpecialListLoadmoreSuccess(bean.getResult().get(0));
                    }
                }else {
                    listener.getSpecialListError(bean.getMessage());
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().getSpecialListError(message);
            }

            @Override
            public void onFinish() {
                getListener().getSpecialListError("获取数据失败!");
            }
        });
    }

    public GetSpecialListView getListener(){
        return listener;
    }

}