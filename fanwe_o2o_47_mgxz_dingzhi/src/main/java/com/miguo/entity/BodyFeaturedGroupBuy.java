package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
public class BodyFeaturedGroupBuy implements Serializable {
    private List<ModelFeaturedGroupBuy> tuan_list;

    public void setTuan_list(List<ModelFeaturedGroupBuy> tuan_list) {
        this.tuan_list = tuan_list;
    }

    public List<ModelFeaturedGroupBuy> getTuan_list() {
        return this.tuan_list;
    }
}
