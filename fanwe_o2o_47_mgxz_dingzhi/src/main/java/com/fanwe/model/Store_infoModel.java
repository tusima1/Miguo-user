package com.fanwe.model;

import java.io.Serializable;
import java.util.List;

public class Store_infoModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String preview;
    private int id;
    private int supplier_id;
    private int is_verify;
    private int is_auto_order;
    private String distance;
    private String avg_point;
    private double xpoint;
    private double ypoint;
    private String address;
    private String name;
    private String tel;
    private int discount_pay;
    private String dp_count;
    private String brief;
    private int offline;
    private List<Store_imagesModel> store_images;
    private String share_url;
    private Float salary_money = 0f;


    public int getOffline() {
        return offline;
    }

    public void setOffline(int offline) {
        this.offline = offline;
    }

    public Float getSalary_money() {
        return salary_money;
    }

    public void setSalary_money(Float salary_money) {
        this.salary_money = salary_money;
    }

    public String getDp_count() {
        return dp_count;
    }

    public void setDp_count(String dp_count) {
        this.dp_count = dp_count;
    }

    public int getDiscount_pay() {
        return discount_pay;
    }

    public void setDiscount_pay(int discount_pay) {
        this.discount_pay = discount_pay;
    }

    public double getXpoint() {
        return xpoint;
    }

    public void setXpoint(double xpoint) {
        this.xpoint = xpoint;
    }

    public double getYpoint() {
        return ypoint;
    }

    public void setYpoint(double ypoint) {
        this.ypoint = ypoint;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public int getIs_verify() {
        return is_verify;
    }

    public void setIs_verify(int is_verify) {
        this.is_verify = is_verify;
    }

    public int getIs_auto_order() {
        return is_auto_order;
    }

    public void setIs_auto_order(int is_auto_order) {
        this.is_auto_order = is_auto_order;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAvg_point() {
        return avg_point;
    }

    public void setAvg_point(String avg_point) {
        this.avg_point = avg_point;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public List<Store_imagesModel> getStore_images() {
        return store_images;
    }

    public void setStore_images(List<Store_imagesModel> store_images) {
        this.store_images = store_images;
    }

}
