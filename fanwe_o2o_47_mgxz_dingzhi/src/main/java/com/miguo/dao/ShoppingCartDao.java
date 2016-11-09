package com.miguo.dao;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/11/9.
 */
public interface ShoppingCartDao {

    void addToShoppingCart(String roomId,String fx_user_id,String lgn_user_id,String goods_id,String cart_type,String add_goods_num);

}
