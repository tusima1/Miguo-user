package com.fanwe.seller.model.getGroupList;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultGroupList {
    private String city_name;

    private String page_title;

    private String quan_id;

    private String cate_id;

    private List<ModelGroupList> body ;

    private String order_type;

    private String info;

    private String city_id;

    public void setCity_name(String city_name){
        this.city_name = city_name;
    }
    public String getCity_name(){
        return this.city_name;
    }
    public void setPage_title(String page_title){
        this.page_title = page_title;
    }
    public String getPage_title(){
        return this.page_title;
    }
    public void setQuan_id(String quan_id){
        this.quan_id = quan_id;
    }
    public String getQuan_id(){
        return this.quan_id;
    }
    public void setCate_id(String cate_id){
        this.cate_id = cate_id;
    }
    public String getCate_id(){
        return this.cate_id;
    }
    public void setBody(List<ModelGroupList> body){
        this.body = body;
    }
    public List<ModelGroupList> getBody(){
        return this.body;
    }
    public void setOrder_type(String order_type){
        this.order_type = order_type;
    }
    public String getOrder_type(){
        return this.order_type;
    }
    public void setInfo(String info){
        this.info = info;
    }
    public String getInfo(){
        return this.info;
    }
    public void setCity_id(String city_id){
        this.city_id = city_id;
    }
    public String getCity_id(){
        return this.city_id;
    }

}
