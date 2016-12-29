package com.miguo.dao;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/26.
 * 首页问候语接口
 */
public interface HomeGreetingDao extends BaseDao{
    void getTodayGreeting(String httpUuid, String token);
}
