package com.fanwe.seller.model.getShopList;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultShopList {
    private List<ModelShopListItem> item;

    private List<ModelShopListNavs> navs;

    private String page_title;

    private String quan_id;

    private String pid;

    private String cate_id;

    private ModelShopListPage page;

    private String store_type;

    private String tid;

    private String city_id;

    public List<ModelShopListItem> getItem() {
        return item;
    }

    public void setItem(List<ModelShopListItem> item) {
        this.item = item;
    }

    public void setNavs(List<ModelShopListNavs> navs) {
        this.navs = navs;
    }

    public List<ModelShopListNavs> getNavs() {
        return this.navs;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    public String getPage_title() {
        return this.page_title;
    }

    public void setQuan_id(String quan_id) {
        this.quan_id = quan_id;
    }

    public String getQuan_id() {
        return this.quan_id;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPid() {
        return this.pid;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getCate_id() {
        return this.cate_id;
    }

    public void setPage(ModelShopListPage page) {
        this.page = page;
    }

    public ModelShopListPage getPage() {
        return this.page;
    }

    public void setStore_type(String store_type) {
        this.store_type = store_type;
    }

    public String getStore_type() {
        return this.store_type;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTid() {
        return this.tid;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_id() {
        return this.city_id;
    }

}
