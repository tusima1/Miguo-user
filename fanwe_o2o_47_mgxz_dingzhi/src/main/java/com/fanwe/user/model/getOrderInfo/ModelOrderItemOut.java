package com.fanwe.user.model.getOrderInfo;

import java.util.List;

/**
 * Created by didik on 2016/8/23.
 */
public class ModelOrderItemOut {
    private String order_status;
    private String create_time;
    private String total_price;
    private String status_name;
    private String pay_amount;
    private String order_id;
    private String order_type;
    private String order_sn;
    /**
     * number : 3
     * refund_status : 0
     * buss_name : 嵊州生活网
     * total_price : 1564.00
     * tuan_id : 32b8820d-13f6-4f14-9c24-355dae972523
     * name : 保定1号团购1
     * consume_count : 0
     * icon :
     * detail_id : ca58198a-c444-4912-8194-93985ca8dfb2
     */

    private List<ModelOrderItemIn> deal_order_item;

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public List<ModelOrderItemIn> getDeal_order_item() {
        return deal_order_item;
    }

    public void setDeal_order_item(List<ModelOrderItemIn> deal_order_item) {
        this.deal_order_item = deal_order_item;
    }
}
