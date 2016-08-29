package com.fanwe.user.model.postUserUpgradeOrder;

/**
 * Created by didik on 2016/8/22.
 */
public class ModelPostUserUpgradeOrder {
    private Config config;

    private String class_name;

    private Order_info order_info;

    public void setConfig(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return this.config;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_name() {
        return this.class_name;
    }

    public void setOrder_info(Order_info order_info) {
        this.order_info = order_info;
    }

    public Order_info getOrder_info() {
        return this.order_info;
    }
}
