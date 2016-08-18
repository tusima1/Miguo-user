package com.miguo.live.model.stopLive;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ModelStopLive implements Serializable {

    private String red_packets_total;
    private String address;
    private String sell_total;
    private String shop_name;
    private String watch_count;
    private String usetime;

    public String getRed_packets_total() {
        return red_packets_total;
    }

    public void setRed_packets_total(String red_packets_total) {
        this.red_packets_total = red_packets_total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSell_total() {
        return sell_total;
    }

    public void setSell_total(String sell_total) {
        this.sell_total = sell_total;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public void setWatch_count(String watch_count) {
        this.watch_count = watch_count;
    }

    public String getWatch_count() {
        return this.watch_count;
    }

    public void setUsetime(String usetime) {
        this.usetime = usetime;
    }

    public String getUsetime() {
        return this.usetime;
    }
}
