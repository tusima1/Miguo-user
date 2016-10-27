package com.miguo.dao;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/26.
 */
public interface HomeGreetingDao extends BaseDao{
    void getTodayGreeting(String token);
}
