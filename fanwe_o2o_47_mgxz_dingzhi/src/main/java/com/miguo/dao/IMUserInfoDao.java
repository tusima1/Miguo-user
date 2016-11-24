package com.miguo.dao;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/7.
 * IM登录成功后更新用户绑定在腾讯上的昵称和头像接口
 * 实现类：{@link com.miguo.dao.impl.IMUserInfoDaoImpl}
 */
public interface IMUserInfoDao {
    /**
     * 更新昵称到腾讯
     * @param nickname
     */
    void updateTencentNickName(String nickname);

    /**
     * 更新头像到腾讯
     * @param avatar
     */
    void updateTencentAvatar(String avatar);
}
