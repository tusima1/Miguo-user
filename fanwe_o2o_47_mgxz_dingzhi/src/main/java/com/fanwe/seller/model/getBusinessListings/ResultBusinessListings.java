package com.fanwe.seller.model.getBusinessListings;

import com.fanwe.base.PageBean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultBusinessListings {

    private List<ModelBusinessListings> shop_list;

    private PageBean page;

    public List<ModelBusinessListings> getShop_list() {
        return shop_list;
    }

    public void setShop_list(List<ModelBusinessListings> shop_list) {
        this.shop_list = shop_list;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }
}
