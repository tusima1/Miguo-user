package com.fanwe.seller.model.getShopList;

import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class Quan_list {
    private String name;

    private String id;

    private List<Quan_sub> quan_sub;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setQuan_sub(List<Quan_sub> quan_sub) {
        this.quan_sub = quan_sub;
    }

    public List<Quan_sub> getQuan_sub() {
        return this.quan_sub;
    }
}
