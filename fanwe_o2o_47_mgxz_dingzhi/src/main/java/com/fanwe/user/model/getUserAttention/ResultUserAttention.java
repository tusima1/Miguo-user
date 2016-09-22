package com.fanwe.user.model.getUserAttention;

import java.util.List;

public class ResultUserAttention {
    private List<ModelUserAttention> body;

    private String title;

    public void setBody(List<ModelUserAttention> body) {
        this.body = body;
    }

    public List<ModelUserAttention> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
