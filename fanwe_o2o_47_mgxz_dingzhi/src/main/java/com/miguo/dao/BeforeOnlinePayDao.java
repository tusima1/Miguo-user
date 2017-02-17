package com.miguo.dao;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/15.
 */

public interface BeforeOnlinePayDao extends BaseDao {
    /**
     * 获取商家线下买单优惠信息
     * @param shopId 商家id
     */
    void getOfflinePayInfo(String shopId);
}
