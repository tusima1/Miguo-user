package com.miguo.dao.impl;

import android.util.Log;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.work.AppRuntimeWorker;
import com.miguo.dao.MessageListDao;
import com.miguo.entity.MenuBean;
import com.miguo.entity.MessageListBean;
import com.miguo.view.BaseView;
import com.miguo.view.MessageListView;

import java.io.IOException;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/20.
 * 二级消息列表
 */
public class MessageListDaoImpl extends BaseDaoImpl implements MessageListDao {

    /**
     * 系统消息列表
     */
    static final int SYSTEM = 1;
    /**
     * 钱款订单消息
     */
    static final int COMMISSION = 2;

    public MessageListDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void getCommissionMessageList(String token) {
        getMessageList(COMMISSION, token);
    }

    @Override
    public void getMessageList(int messageType, String token) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("method", "MessageList");
        map.put("message_type", messageType + "");
        map.put("token", token);
        OkHttpUtil.getInstance().get("", map, new MgCallback(MessageListBean.class) {

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.d(tag, responseBody);
            }

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                MessageListBean bean = (MessageListBean)responseBody;
                if(isEmpty(bean)){
                    getListener().getMessageListError(BASE_ERROR_MESSAGE);
                    return;
                }
                if(bean.getStatusCode() != 200){
                    getListener().getMessageListError(bean.getMessage());
                    return;
                }
                if(bean.getStatusCode() == 200){
                    try{
                        getListener().getMessageListSuccess(bean.getResult().get(0).getBody());
                        return;
                    }catch (Exception e){
                        getListener().getMessageListError(BASE_ERROR_MESSAGE);
                        return;
                    }
                }

                getListener().getMessageListError(bean.getMessage());
            }
            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().getMessageListError(BASE_ERROR_MESSAGE);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getListener().getMessageListError(BASE_ERROR_MESSAGE);
            }

        });
    }

    @Override
    public void getSystemMessageList(String token) {
        getMessageList(SYSTEM, token);
    }

    @Override
    public MessageListView getListener() {
        return (MessageListView)baseView;
    }
}
