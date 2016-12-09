package com.miguo.dao;

import com.fanwe.shoppingcart.model.ShoppingCartInfo;

import java.util.List;

/**
 * Created by zlh on 2016/12/6.
 * 批量添加购物车
 * 将本地离线购物车添加到线上购物车
 */
public interface ShoppingCartMultiAddDao extends BaseDao {

    /**
     * 正常批量添加购物车
     * @param datas 购物车列表
     */
    void multiAddFromShoppingCart(List<ShoppingCartInfo> datas);

    /**
     * 离线添加批量购物车
     * @param datas 购物车列表
     */
    void multiAddFromOther(List<ShoppingCartInfo> datas);
}
