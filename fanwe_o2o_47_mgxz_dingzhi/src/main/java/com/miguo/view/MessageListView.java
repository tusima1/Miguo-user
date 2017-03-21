package com.miguo.view;

import com.miguo.entity.MessageListBean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/20.
 */

public interface MessageListView extends BaseView {

    /**
     * 获取消息列表成功
     * @param messages 消息列表
     */
    void getMessageListSuccess(List<MessageListBean.Result.Body> messages);

    /**
     * 获取消息列表成功，加载更多
     * @param messages 消息列表
     */
    void getMessageListLoadmoreSuccess(List<MessageListBean.Result.Body> messages);

    /**
     * 获取消息列表失败
     * @param message 失败回调消息
     */
    void getMessageListError(String message);

}
