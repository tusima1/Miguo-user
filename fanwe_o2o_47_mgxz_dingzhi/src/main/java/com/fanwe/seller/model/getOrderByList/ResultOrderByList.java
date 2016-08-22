package com.fanwe.seller.model.getOrderByList;

import com.fanwe.seller.model.getShopList.ModelShopListNavs;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultOrderByList {

    private List<ModelShopListNavs> body;

    private String title;

    public List<ModelShopListNavs> getBody() {
        return body;
    }

    public void setBody(List<ModelShopListNavs> body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
