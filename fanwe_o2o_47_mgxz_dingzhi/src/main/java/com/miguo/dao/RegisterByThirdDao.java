package com.miguo.dao;

/**
 * Created by zlh on 2016/12/5.
 * 第三方登录后绑定手机号码
 */
public interface RegisterByThirdDao extends BaseDao {

    /**
     *
     * @param mobile 手机号码
     * @param openId 第三方登录后的openId
     * @param smdCode 短信验证码
     * @param icon 第三方登录返回的用户头像
     * @param nick 第三方登录返回的昵称
     * @param platform 第三方平台
     * @param shareId 分享id（如果有的话）
     */
    void registerByThird(String mobile, String openId, String smdCode, String icon, String nick, String platform, String shareId);

}
