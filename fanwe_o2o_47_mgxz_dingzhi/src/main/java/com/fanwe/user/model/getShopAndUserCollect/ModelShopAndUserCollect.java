package com.fanwe.user.model.getShopAndUserCollect;

import java.util.List;

public class ModelShopAndUserCollect {
    private List<Image> img_arr;

    private String index_img;

    private String cons_count;

    private String collect_type;

    private String coll_count;

    private String id;

    private String mobile_brief;

    private String shop_name;

    public void setImg_arr(List<Image> img_arr) {
        this.img_arr = img_arr;
    }

    public List<Image> getImg_arr() {
        return this.img_arr;
    }

    public void setIndex_img(String index_img) {
        this.index_img = index_img;
    }

    public String getIndex_img() {
        return this.index_img;
    }

    public void setCons_count(String cons_count) {
        this.cons_count = cons_count;
    }

    public String getCons_count() {
        return this.cons_count;
    }

    public void setCollect_type(String collect_type) {
        this.collect_type = collect_type;
    }

    public String getCollect_type() {
        return this.collect_type;
    }

    public void setColl_count(String coll_count) {
        this.coll_count = coll_count;
    }

    public String getColl_count() {
        return this.coll_count;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setMobile_brief(String mobile_brief) {
        this.mobile_brief = mobile_brief;
    }

    public String getMobile_brief() {
        return this.mobile_brief;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_name() {
        return this.shop_name;
    }
}
