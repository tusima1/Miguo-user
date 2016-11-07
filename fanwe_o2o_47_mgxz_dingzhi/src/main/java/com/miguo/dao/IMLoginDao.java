package com.miguo.dao;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/7.
 * IM登录接口（进直播需要）
 * 回调View：{@link com.miguo.view.IMLoginView}
 * 接口实现：{@link com.miguo.dao.impl.TencentSignDaoImpl}
 */
public interface IMLoginDao extends BaseDao{
    /**
     *
     * @param identify 用户id
     * @param userSig 用户签名
     */
    void imLogin(String identify, String userSig);
}
