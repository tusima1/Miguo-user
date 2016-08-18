package com.fanwe.seller.model.checkShopCollect;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultCheckShopCollect {

    private List<ModelCheckShopCollect> body;

    private String title;

    public void setBody(List<ModelCheckShopCollect> body) {
        this.body = body;
    }

    public List<ModelCheckShopCollect> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
