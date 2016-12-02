package com.miguo.dao;

/**
 * Created by zlh on 2016/12/2.
 * 验证码快速登录
 */
public interface LoginByMobilleWithSMSDao extends BaseDao {

    /**
     *
     * @param mobile 手机号
     * @param smsCode 验证码
     * @param shareCode 分享id
     */
    void loginByMobileWithSMS(String mobile, String smsCode, String shareCode);

}
