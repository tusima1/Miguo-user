package com.fanwe.user.model.getShopAndUserCollect;

import java.util.List;

public class ResultShopAndUserCollect {
    private List<ModelShopAndUserCollect> body;

    private String title;

    public void setBody(List<ModelShopAndUserCollect> body) {
        this.body = body;
    }

    public List<ModelShopAndUserCollect> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
