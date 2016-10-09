package com.fanwe.shoppingcart.model;

import java.io.Serializable;

/**
 * 支付方式实体类。
 * Created by Administrator on 2016/8/23.
 */
public class PaymentTypeInfo implements Serializable {

    private String online_pay;

    private String fee_amount;

    private String total_amount;

    private String name;

    private String is_effect;

    private String description;

    private String logo;

    private String id;

    private String sort;

    private String fee_type;

    private String class_name;

    private String config;

    private boolean checked=false;

    private String default_pay="0";

    public void setOnline_pay(String online_pay){
        this.online_pay = online_pay;
    }
    public String getOnline_pay(){
        return this.online_pay;
    }
    public void setFee_amount(String fee_amount){
        this.fee_amount = fee_amount;
    }
    public String getFee_amount(){
        return this.fee_amount;
    }
    public void setTotal_amount(String total_amount){
        this.total_amount = total_amount;
    }
    public String getTotal_amount(){
        return this.total_amount;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setIs_effect(String is_effect){
        this.is_effect = is_effect;
    }
    public String getIs_effect(){
        return this.is_effect;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }
    public void setLogo(String logo){
        this.logo = logo;
    }
    public String getLogo(){
        return this.logo;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setSort(String sort){
        this.sort = sort;
    }
    public String getSort(){
        return this.sort;
    }
    public void setFee_type(String fee_type){
        this.fee_type = fee_type;
    }
    public String getFee_type(){
        return this.fee_type;
    }
    public void setClass_name(String class_name){
        this.class_name = class_name;
    }
    public String getClass_name(){
        return this.class_name;
    }
    public void setConfig(String config){
        this.config = config;
    }
    public String getConfig(){
        return this.config;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getDefault_pay() {
        return default_pay;
    }

    public void setDefault_pay(String default_pay) {
        this.default_pay = default_pay;
        if("1".equals(default_pay)){
            this.checked = true;
        }
    }
}