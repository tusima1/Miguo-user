package com.miguo.dao;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/21.
 * 商家详情收藏接口
 */
public interface CollectShopDao extends BaseDao{

    void collectShop(String merchantId);
    void unCollectShop(String merchantId);

}
