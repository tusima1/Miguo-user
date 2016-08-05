package com.fanwe.seller.model.getShopList;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultShopList {

    private List<Quan_list> quan_list;

    private List<ModelShopList> item;

    private String city_name;

    private List<Bcate_list> bcate_list;

    private List<Navs> navs;

    private String page_title;

    private String quan_id;

    private String cate_id;

    private Page page;

    private String area_id;

    private String city_id;

    public List<Quan_list> getQuan_list() {
        return quan_list;
    }

    public void setQuan_list(List<Quan_list> quan_list) {
        this.quan_list = quan_list;
    }

    public List<Bcate_list> getBcate_list() {
        return bcate_list;
    }

    public void setBcate_list(List<Bcate_list> bcate_list) {
        this.bcate_list = bcate_list;
    }

    public void setItem(List<ModelShopList> item) {
        this.item = item;
    }

    public List<ModelShopList> getItem() {
        return this.item;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_name() {
        return this.city_name;
    }


    public void setNavs(List<Navs> navs) {
        this.navs = navs;
    }

    public List<Navs> getNavs() {
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

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getCate_id() {
        return this.cate_id;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Page getPage() {
        return this.page;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getArea_id() {
        return this.area_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_id() {
        return this.city_id;
    }
}
