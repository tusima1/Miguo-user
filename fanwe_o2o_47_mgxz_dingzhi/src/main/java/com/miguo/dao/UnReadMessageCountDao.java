package com.miguo.dao;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/23.
 * 获取未读消息数量
 */
public interface UnReadMessageCountDao extends BaseDao {

    void getUnReadMessageCount(String token);

}
