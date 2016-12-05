package com.fanwe.shoppingcart.model;

import java.io.Serializable;

/**
 * 购物车实体类
 * Created by Administrator on 2016/8/18.
 */
public class ShoppingCartInfo implements Serializable {
    /**
     * 购买 数量
     */
    private String number;
    /**
     * 团购价格
     */
    private String tuan_price;

    /**
     * 图片。
     */
    private String img;

    /**
     * 是否允许购买 。用于判断该商品还能不能买，1为可以购买，0为不可
     */
    private String buyFlg;
    /**
     * 商品ID
     */

    private String id;
    /**
     * 原价
     */

    private String origin_price;
    /**
     * 标题 。
     */
    private String title;
    /**
     * 最大购买 量。
     */

    private String limit_num;

    /**
     * 是否被选中。
     */
    private boolean isChecked = false;


    private String is_first;
    /**
     * 首单 优惠价。
     */
    private String is_first_price;

    /**
     * 代言人ID、
     */
    private String fx_user_id;

    /**
     * 小计总额。
     */
    private float sumPrice = 0.00f;
    /**
     * 团购券ID。
     */
    private String pro_id;
    private String share_record_id;//分享id

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return this.number;
    }

    public void setTuan_price(String tuan_price) {
        this.tuan_price = tuan_price;
    }

    public String getTuan_price() {
        return this.tuan_price;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return this.img;
    }

    public void setBuyFlg(String buyFlg) {
        this.buyFlg = buyFlg;
    }

    public String getBuyFlg() {
        return this.buyFlg;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setOrigin_price(String origin_price) {
        this.origin_price = origin_price;
    }

    public String getOrigin_price() {
        return this.origin_price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setLimit_num(String limit_num) {
        this.limit_num = limit_num;
    }

    public String getLimit_num() {
        return this.limit_num;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public float getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(float sumPrice) {
        this.sumPrice = sumPrice;
    }

    public String getIs_first() {
        return is_first;
    }

    public void setIs_first(String is_first) {
        this.is_first = is_first;
    }

    public String getIs_first_price() {
        return is_first_price;
    }

    public void setIs_first_price(String is_first_price) {
        this.is_first_price = is_first_price;
    }

    public String getFx_user_id() {
        return fx_user_id;
    }

    public void setFx_user_id(String fx_user_id) {
        this.fx_user_id = fx_user_id;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getShare_record_id() {
        return share_record_id;
    }

    public void setShare_record_id(String share_record_id) {
        this.share_record_id = share_record_id;
    }
}
