package com.miguo.dao;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/7.
 * 对应的callbackView：{@link com.miguo.view.LoginByMobileView}
 * 当前实现类：{@link com.miguo.dao.impl.LoginByMobileDaoImpl}
 */
public interface LoginByMobileDao {
    void loginByMobile(String mobile, String password);
}
