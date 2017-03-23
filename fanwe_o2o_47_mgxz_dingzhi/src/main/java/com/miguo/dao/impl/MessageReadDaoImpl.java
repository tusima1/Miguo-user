package com.miguo.dao.impl;

import android.util.Log;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.miguo.dao.MessageReadDao;
import com.miguo.entity.UnReadMessageCountBean;
import com.miguo.view.BaseView;
import com.miguo.view.MessageReadView;

import java.util.TreeMap;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/23.
 */

public class MessageReadDaoImpl extends BaseDaoImpl implements MessageReadDao {

    public MessageReadDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void messageRead(String message_id) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("method", "MessageRead");
        map.put("message_id", message_id);
        OkHttpUtil.getInstance().get("", map, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
            }

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {

            }
        });

    }

    @Override
    public MessageReadView getListener() {
        return (MessageReadView)baseView;
    }
}
