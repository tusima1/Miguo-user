package com.miguo.live.model.getLiveListNew;

/**
 * 位置信息
 * Created by Administrator on 2016/7/27.
 */
public class ModelLbs {
    //                "lbs": {                                                                        门店信息
//            "geo_y": "0",                                                                经纬度
//            "geo_x": "0",
//                    "shop_id": "4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b2",                           门店id
//            "address": "浦沿",                                                           地址
//            "shop_name": "爆米花"                                                        门店名称
//        }
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
