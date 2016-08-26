package com.fanwe.model;

public class StoreIn_list {
    private String id;
    private String name;
    private String img;
    private Float current_price;
    private Float salary;
    private Float origin_price;
    private String is_delete;

    private String max_num;
    private int is_first;
    private float is_first_price;
    private String sub_name;
    private int time_status;

    public String getMax_num() {
        return max_num;
    }

    public void setMax_num(String max_num) {
        this.max_num = max_num;
    }

    public int getIs_first() {
        return is_first;
    }

    public void setIs_first(int is_first) {
        this.is_first = is_first;
    }

    public float getIs_first_price() {
        return is_first_price;
    }

    public void setIs_first_price(float is_first_price) {
        this.is_first_price = is_first_price;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public int getTime_status() {
        return time_status;
    }

    public void setTime_status(int time_status) {
        this.time_status = time_status;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

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
