package com.miguo.dao;

/**
 * Created by zlh on 2016/12/3.
 */
public interface RegisterByMobileDao extends BaseDao {
    /**
     * 手机号码注册
     * @param mobile 手机号码
     * @param smsCode 验证码
     * @param password 密码
     * @param shareCode 分享id
     */
    void registerByMobile(String mobile, String smsCode, String password, String shareCode);
}
