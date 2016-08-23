package com.fanwe.seller.model.getShopInfo;

import com.fanwe.seller.model.ModelComment;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultShopInfo {
    private String city_name;
    private String page_title;
    private StoreModelShopInfo store_info;
    private List<GoodsModelShopInfo> tuan_list;
    private List<StoreModelShopInfo> other_supplier_location;
    private List<ModelComment> dp_list;

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getPage_title() {
        return page_title;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    public StoreModelShopInfo getStore_info() {
        return store_info;
    }

    public void setStore_info(StoreModelShopInfo store_info) {
        this.store_info = store_info;
    }

    public List<GoodsModelShopInfo> getTuan_list() {
        return tuan_list;
    }

    public void setTuan_list(List<GoodsModelShopInfo> tuan_list) {
        this.tuan_list = tuan_list;
    }

    public List<StoreModelShopInfo> getOther_supplier_location() {
        return other_supplier_location;
    }

    public void setOther_supplier_location(List<StoreModelShopInfo> other_supplier_location) {
        this.other_supplier_location = other_supplier_location;
    }

    public List<ModelComment> getDp_list() {
        return dp_list;
    }

    public void setDp_list(List<ModelComment> dp_list) {
        this.dp_list = dp_list;
    }
}
