package com.fanwe.shoppingcart.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 订单实体类。
 * Created by Administrator on 2016/8/25.
 */
public class OrderDetailInfo  implements Serializable{

    private Order_info order_info;

    private String class_name;

    private HashMap<String,String> config;

    public void setOrder_info(Order_info order_info){
        this.order_info = order_info;
    }
    public Order_info getOrder_info(){
        return this.order_info;
    }
    public void setClass_name(String class_name){
        this.class_name = class_name;
    }
    public String getClass_name(){
        return this.class_name;
    }

    public HashMap<String, String> getConfig() {
        return config;
    }

    public void setConfig(HashMap<String, String> config) {
        this.config = config;
    }
}
