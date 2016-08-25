package com.fanwe.user.model.getGroupBuyCoupon;

/**
 * Created by didik on 2016/8/25.
 */
public class ModelShopInfo2 {
//    "shop_id": "4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b2",      门店id
//    "address": "浦沿",                                      门店地址
//    "index_img": "",                                        门店图片地址
//    "tel": "",                                             门店电话
//    "shop_name": "爆米花"                                  门店名称

    private String shop_id;
    private String address;
    private String index_img;
    private String tel;
    private String shop_name;

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

    public String getIndex_img() {
        return index_img;
    }

    public void setIndex_img(String index_img) {
        this.index_img = index_img;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }
}
