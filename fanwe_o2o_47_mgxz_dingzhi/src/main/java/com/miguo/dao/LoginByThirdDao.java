package com.miguo.dao;

/**
 * Created by zlh on 2016/11/30.
 */

public interface LoginByThirdDao extends BaseDao {
    /**
     * 第三方登录
     * @param openId
     * @param platformType
     * @param icon
     * @param nick
     */
    void thirdLogin(String openId,String platformType, String icon, String nick);
}
