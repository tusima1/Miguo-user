package com.miguo.dao.impl;

import android.util.Log;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.miguo.dao.UnReadMessageCountDao;
import com.miguo.definition.PageSize;
import com.miguo.entity.MessageListBean;
import com.miguo.entity.UnReadMessageCountBean;
import com.miguo.view.BaseView;
import com.miguo.view.UnReadMessageCountView;

import java.io.IOException;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/23.
 * 获取未读消息数量
 */
public class UnReadMessageCountDaoImpl extends BaseDaoImpl implements UnReadMessageCountDao {

    public UnReadMessageCountDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void getUnReadMessageCount(String token) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("method", "UnReadMessageCount");
        map.put("token", token);
        OkHttpUtil.getInstance().get("", map, new MgCallback(UnReadMessageCountBean.class) {

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.d(tag, responseBody);
            }

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                UnReadMessageCountBean bean = (UnReadMessageCountBean)responseBody;
                if(isEmpty(bean)){
                    getListener().getUnReadMessageCountError(BASE_ERROR_MESSAGE);
                    return;
                }
                if(bean.getStatusCode() != 200){
                    getListener().getUnReadMessageCountError(bean.getMessage());
                    return;
                }
                if(bean.getStatusCode() == 200){
                    try{
                        getListener().getUnReadMessageCountSuccess(bean.getResult().get(0).getBody().get(0));
                        return;
                    }catch (Exception e){
                        getListener().getUnReadMessageCountError(bean.getMessage());
                        return;
                    }
                }

                getListener().getUnReadMessageCountError(bean.getMessage());
            }
            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().getUnReadMessageCountError(BASE_ERROR_MESSAGE);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getListener().getUnReadMessageCountError(BASE_ERROR_MESSAGE);
            }

        });
    }

    @Override
    public UnReadMessageCountView getListener() {
        return (UnReadMessageCountView)baseView;
    }
}
