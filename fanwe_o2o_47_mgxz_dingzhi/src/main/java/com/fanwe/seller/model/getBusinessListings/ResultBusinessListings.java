package com.fanwe.seller.model.getBusinessListings;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultBusinessListings {

    private List<ModelBusinessListings> shop_list;

    private String p_name;

    private String cate_name;

    public void setShop_list(List<ModelBusinessListings> shop_list) {
        this.shop_list = shop_list;
    }

    public List<ModelBusinessListings> getShop_list() {
        return this.shop_list;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_name() {
        return this.p_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getCate_name() {
        return this.cate_name;
    }


}
