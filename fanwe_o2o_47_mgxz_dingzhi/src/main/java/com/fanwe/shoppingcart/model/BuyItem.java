package com.fanwe.shoppingcart.model;

import java.io.Serializable;

/**
 * 购物车确认的商品详情。
 * Created by Administrator on 2016/8/22.
 */
public class BuyItem  implements Serializable{
    private String number;

    private String img;

    private String goodsId;

    private String price;

    private String id;

    private String type;

    private String goodsName;

    private String total;

    public void setNumber(String number){
        this.number = number;
    }
    public String getNumber(){
        return this.number;
    }
    public void setImg(String img){
        this.img = img;
    }
    public String getImg(){
        return this.img;
    }
    public void setGoodsId(String goodsId){
        this.goodsId = goodsId;
    }
    public String getGoodsId(){
        return this.goodsId;
    }
    public void setPrice(String price){
        this.price = price;
    }
    public String getPrice(){
        return this.price;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setGoodsName(String goodsName){
        this.goodsName = goodsName;
    }
    public String getGoodsName(){
        return this.goodsName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}