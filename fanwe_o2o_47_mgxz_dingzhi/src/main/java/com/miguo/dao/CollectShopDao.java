package com.miguo.dao;

/**
 * Created by Administrator on 2016/10/21.
 */
public interface CollectShopDao extends BaseDao{

    void collectShop(String merchantId);
    void unCollectShop(String merchantId);

}
