package com.fanwe.dao.barry;

/**
 * Created by Barry on 2016/10/12.
 * 添加商品到购物车
 */
public interface ShappingCartDao {
    void addSaleToShappingCart(String roomId,String fx_user_id,String lgn_user_id,String goods_id,String cart_type,String add_goods_num);
}
