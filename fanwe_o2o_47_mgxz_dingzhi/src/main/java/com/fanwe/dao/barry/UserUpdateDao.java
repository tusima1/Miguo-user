package com.fanwe.dao.barry;

/**
 * Created by 狗蛋哥/zlh on 2016/9/28.
 * 用户升级支付接口
 */
public interface UserUpdateDao {

    void getUserUpdateInfo(String token);
    void updateUserLevel(String payment_id);
}
