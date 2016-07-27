package com.fanwe.home.model;

/**
 * 位置信息
 * Created by Administrator on 2016/7/27.
 */
public class Lbs {
    private int longitude;

    private int latitude;

    private String address;

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLongitude() {
        return this.longitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLatitude() {
        return this.latitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }
}
