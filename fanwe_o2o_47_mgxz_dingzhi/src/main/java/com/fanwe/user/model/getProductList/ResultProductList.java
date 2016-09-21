package com.fanwe.user.model.getProductList;

import java.util.List;

public class ResultProductList {
    private List<ModelProductList> body;

    private String title;

    public void setBody(List<ModelProductList> body) {
        this.body = body;
    }

    public List<ModelProductList> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
