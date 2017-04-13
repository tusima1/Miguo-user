package com.miguo.dao.impl;

import android.util.Log;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.miguo.dao.TouTiaoDao;
import com.miguo.definition.PageSize;
import com.miguo.entity.MessageListBean;
import com.miguo.entity.ToutiaoBean;
import com.miguo.view.BaseView;
import com.miguo.view.TouTiaoView;

import java.io.IOException;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * Created by Barry on 2017/4/13.
 */
public class TouTiaoDaoImpl extends BaseDaoImpl implements TouTiaoDao{

    public TouTiaoDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void getToutiao() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("method", "GetHeadlineList");
        OkHttpUtil.getInstance().get("", map, new MgCallback(ToutiaoBean.class) {

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.d(tag, responseBody);
            }

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                ToutiaoBean bean = (ToutiaoBean)responseBody;
                if(isEmpty(bean)){
                    getListener().getToutiaoListError(BASE_ERROR_MESSAGE);
                    return;
                }
                if(bean.getStatusCode() != 200){
                    getListener().getToutiaoListError(bean.getMessage());
                    return;
                }
                if(bean.getStatusCode() == 200){
                    try{
                        getListener().getToutiaoListSuccess(bean.getResult().get(0).getBody());
                        return;
                    }catch (Exception e){
                        getListener().getToutiaoListError(BASE_ERROR_MESSAGE);
                        return;
                    }
                }

                getListener().getToutiaoListError(bean.getMessage());
            }
            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().getToutiaoListError(BASE_ERROR_MESSAGE);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getListener().getToutiaoListError(BASE_ERROR_MESSAGE);
            }

        });
    }

    @Override
    public TouTiaoView getListener() {
        return (TouTiaoView)baseView;
    }
}
