package com.fanwe.user.model.getAttentionFocus;

import java.util.List;

public class ResultAttentionFocus {
    private List<ModelAttentionFocus> body;

    private String title;

    public void setBody(List<ModelAttentionFocus> body) {
        this.body = body;
    }

    public List<ModelAttentionFocus> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
