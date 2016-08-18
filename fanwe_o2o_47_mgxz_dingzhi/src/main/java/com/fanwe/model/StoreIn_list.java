package com.fanwe.model;

public class StoreIn_list {
    private String id;
    private String name;
    private String img;
    private Float current_price;
    private Float salary;
    private Float origin_price;


    public Float getOrigin_price() {
        return origin_price;
    }

    public void setOrigin_price(Float origin_price) {
        this.origin_price = origin_price;
    }

    public Float getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(Float current_price) {
        this.current_price = current_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }
}
