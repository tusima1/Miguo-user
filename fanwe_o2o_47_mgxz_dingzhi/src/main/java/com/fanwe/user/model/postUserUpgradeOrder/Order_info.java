package com.fanwe.user.model.postUserUpgradeOrder;

/**
 * Created by Administrator on 2016/8/29.
 */
public class Order_info {
    private String order_status;

    private String sub_name;

    private String total_price;

    private String payment_id;

    private String name;

    private String order_id;

    private String order_type;

    private String order_sn;

    public void setOrder_status(String order_status){
        this.order_status = order_status;
    }
    public String getOrder_status(){
        return this.order_status;
    }
    public void setSub_name(String sub_name){
        this.sub_name = sub_name;
    }
    public String getSub_name(){
        return this.sub_name;
    }
    public void setTotal_price(String total_price){
        this.total_price = total_price;
    }
    public String getTotal_price(){
        return this.total_price;
    }
    public void setPayment_id(String payment_id){
        this.payment_id = payment_id;
    }
    public String getPayment_id(){
        return this.payment_id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setOrder_id(String order_id){
        this.order_id = order_id;
    }
    public String getOrder_id(){
        return this.order_id;
    }
    public void setOrder_type(String order_type){
        this.order_type = order_type;
    }
    public String getOrder_type(){
        return this.order_type;
    }
    public void setOrder_sn(String order_sn){
        this.order_sn = order_sn;
    }
    public String getOrder_sn(){
        return this.order_sn;
    }

}
