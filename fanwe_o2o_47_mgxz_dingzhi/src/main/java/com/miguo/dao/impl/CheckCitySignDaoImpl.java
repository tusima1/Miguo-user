package com.miguo.dao.impl;

import android.util.Log;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.miguo.dao.CheckCitySignDao;
import com.miguo.entity.CheckCitySignBean;
import com.miguo.view.BaseView;
import com.miguo.view.CheckCityView;

import java.io.IOException;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/9.
 */
public class CheckCitySignDaoImpl extends BaseDaoImpl implements CheckCitySignDao{

    public CheckCitySignDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public CheckCityView getListener() {
        return (CheckCityView)baseView;
    }

    @Override
    public void checkCitySign(String city_id) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("method", "CheckCitySign");
        map.put("city_id", city_id);
        OkHttpUtil.getInstance().get("", map, new MgCallback(CheckCitySignBean.class) {

            @Override
            public void onSuccessResponse(String responseBody) {

            }

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                CheckCitySignBean bean = (CheckCitySignBean)responseBody;
                if(bean != null){
                    if(bean.getStatusCode() == 200){
                        try{
                            if(bean.getResult().get(0).getBody().get(0).getIs_sign() == 1){
                                getListener().checkCitySignSuccess();
                                return;
                            }
                            if(bean.getResult().get(0).getBody().get(0).getIs_sign() == 0){
                                getListener().checkCitySignError(bean.getResult().get(0).getBody().get(0));
                                return;
                            }
                        }catch (Exception e){
                            getListener().networkError();
                        }
                    }else {
                        getListener().networkError();
                    }
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().networkError();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getListener().networkError();
            }

        });
    }
}
