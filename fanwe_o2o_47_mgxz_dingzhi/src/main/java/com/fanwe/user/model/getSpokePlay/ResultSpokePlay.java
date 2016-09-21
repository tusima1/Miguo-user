package com.fanwe.user.model.getSpokePlay;

import java.util.List;

public class ResultSpokePlay {
    private List<ModelSpokePlay> body;

    private String title;

    public void setBody(List<ModelSpokePlay> body) {
        this.body = body;
    }

    public List<ModelSpokePlay> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
