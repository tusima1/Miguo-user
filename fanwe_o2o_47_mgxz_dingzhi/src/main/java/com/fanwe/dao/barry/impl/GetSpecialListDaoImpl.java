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
    public void getSpecialList(final String httpUuid,String city_id, String cur_geo_x, String cur_geo_y, final String page) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("token", App.getInstance().getToken());
        params.put("method", LiveConstants.SPECIAL_LIST);
        params.put("city_id", city_id);
        params.put("cur_geo_x", cur_geo_x);
        params.put("cur_geo_y", cur_geo_y);
        params.put("page", page);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                SpecialListModel bean = new Gson().fromJson(responseBody, SpecialListModel.class);
                if(bean.getStatusCode() == 200){
                    if(bean.getResult().get(0) != null && bean.getResult().get(0).getBody()!=null){
                        for(SpecialListModel.Result.Body  body : bean.getResult().get(0).getBody()){
                            body.setCount_down(bean.getResult().get(0).getCount_down());
                        }
                    }
                    if(page.equals("0") || page.equals("1")){
                        listener.getSpecialListSuccess(httpUuid,bean.getResult().get(0));
                    }else {
                        listener.getSpecialListLoadmoreSuccess(httpUuid, bean.getResult().get(0));
                    }
                }else {
                    listener.getSpecialListNoData(httpUuid, bean.getMessage());
                }
            }

            @Override
            public void onErrorResponseOnMainThread(String message, String errorCode) {
                getListener().getSpecialListError(httpUuid, message);
            }

        });
    }

    public GetSpecialListView getListener(){
        return listener;
    }

}
