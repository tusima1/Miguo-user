package com.miguo.live.model.payHistory;

/**
 * 充值记录展示类
 * Created by Administrator on 2016/9/11.
 */
public class ModelPayHistory {
    private String id;
    //title;item
    private int type;
    private String price;
    private String diamond_count;
    private String order_id;
    private String status;
    private String date;
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiamond_count() {
        return diamond_count;
    }

    public void setDiamond_count(String diamond_count) {
        this.diamond_count = diamond_count;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
