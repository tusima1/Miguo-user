package com.miguo.dao;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/21.
 * 商家详情页接口
 */
public interface HiShopDetailDao extends BaseDao{

    void getShopDetail(String shop_id, String m_longitude, String m_latitude);

}
