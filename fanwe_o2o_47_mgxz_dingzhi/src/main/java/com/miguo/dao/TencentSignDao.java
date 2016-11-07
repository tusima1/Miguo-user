package com.miguo.dao;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/7.
 * 获取腾讯签名接口
 * 回调View：{@link com.miguo.view.TencentSignView}
 * 接口实现：{@link com.miguo.dao.impl.TencentSignDaoImpl}
 */
public interface TencentSignDao extends BaseDao{

    void getTencentSign(String token);

}
