package com.fanwe.home.model;

/**
 * 位置信息
 * Created by Administrator on 2016/7/27.
 */
public class Lbs {
    private String geo_y;

    private String geo_x;

    private String shop_id;

    private String address;

    private String shop_name;

    public String getGeo_y() {
        return geo_y;
    }

    public void setGeo_y(String geo_y) {
        this.geo_y = geo_y;
    }

    public String getGeo_x() {
        return geo_x;
    }

    public void setGeo_x(String geo_x) {
        this.geo_x = geo_x;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }
}
