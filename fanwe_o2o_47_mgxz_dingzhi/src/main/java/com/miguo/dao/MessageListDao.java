package com.miguo.dao;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/20.
 * 二级消息列表
 */
public interface MessageListDao extends BaseDao {

    /**
     * 获取二级消息列表
     * @param messageType 1 系统消息、2 钱款-订单-代言消息
     * @param token 用户登录token
     */
    void getMessageList(int page, int page_size, int messageType, String token);

    /**
     * 获取系统消息
     * @param token 用户登录token
     */
    void getSystemMessageList(int page, int page_size, String token);

    /**
     * 获取钱款 订单 代言消息
     * @param token 用户登录token
     */
    void getCommissionMessageList(int page, int page_size, String token);

}
