package com.fanwe.user.model.wallet;

import java.io.Serializable;

/**
 * 兑换果钻列表。
 * Created by zhouhy on 2016/11/23.
 */

public class Convert_list implements Serializable {

    private String diamond;

    private String id;

    private String sort;

    private String bean;

    private boolean exchangeAble=false;

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }

    public String getDiamond() {
        return this.diamond;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return this.sort;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getBean() {
        return this.bean;
    }

    public boolean isExchangeAble() {
        return exchangeAble;
    }

    public void setExchangeAble(boolean exchangeAble) {
        this.exchangeAble = exchangeAble;
    }
}
