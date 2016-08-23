package com.fanwe.shoppingcart.model;

import java.io.Serializable;


import java.util.List;
/**
 * 商家列表。
 * Created by Administrator on 2016/8/22.
 */
public class Deals  implements Serializable{
    private String entName;

    private String shipping;

    private List<BuyItem> buyItem ;

    public void setEntName(String entName){
        this.entName = entName;
    }
    public String getEntName(){
        return this.entName;
    }
    public void setShipping(String shipping){
        this.shipping = shipping;
    }
    public String getShipping(){
        return this.shipping;
    }
    public void setBuyItem(List<BuyItem> buyItem){
        this.buyItem = buyItem;
    }
    public List<BuyItem> getBuyItem(){
        return this.buyItem;
    }

}