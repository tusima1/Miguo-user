package com.fanwe.user.model.getPersonHomePage;

import java.util.List;

public class ResultPersonHomePage {
    private List<ModelPersonHomePage> body;

    private String title;

    public void setBody(List<ModelPersonHomePage> body) {
        this.body = body;
    }

    public List<ModelPersonHomePage> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
