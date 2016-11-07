package com.miguo.dao;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/7.
 * 使用分享领取码进入房间并领钻
 * 回调callback {@link com.miguo.view.GetUserReceiveCodeView}
 * 实现impl {@link com.miguo.dao.impl.GetUserReceiveCodeDaoImpl}
 */
public interface GetUserReceiveCodeDao extends BaseDao{

    void getUserReceiveCode(String receive_code);

}
