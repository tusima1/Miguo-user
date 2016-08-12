package com.fanwe.seller.model.getBusinessCircleList;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ModelBusinessCircleList {
    private String name;

    private String id;

    private List<ModelQuanSub> quan_sub;

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

    public List<ModelQuanSub> getQuan_sub() {
        return quan_sub;
    }

    public void setQuan_sub(List<ModelQuanSub> quan_sub) {
        this.quan_sub = quan_sub;
    }
}
