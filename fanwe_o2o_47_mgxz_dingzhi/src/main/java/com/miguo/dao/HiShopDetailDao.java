package com.miguo.dao;

/**
 * Created by Administrator on 2016/10/21.
 */
public interface HiShopDetailDao extends BaseDao{

    void getShopDetail(String shop_id, String m_longitude, String m_latitude);

}
