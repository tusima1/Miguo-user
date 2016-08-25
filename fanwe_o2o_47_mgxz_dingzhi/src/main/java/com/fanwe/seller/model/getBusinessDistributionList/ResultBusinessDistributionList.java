package com.fanwe.seller.model.getBusinessDistributionList;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultBusinessDistributionList {

    private String is_myshop;
    private List<ModelBusinessDistributionList> list;

    public void setIs_myshop(String is_myshop) {
        this.is_myshop = is_myshop;
    }

    public String getIs_myshop() {
        return this.is_myshop;
    }

    public void setList(List<ModelBusinessDistributionList> list) {
        this.list = list;
    }

    public List<ModelBusinessDistributionList> getList() {
        return this.list;
    }

}
