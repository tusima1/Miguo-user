package com.miguo.live.model.getStoresRandomComment;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultStoresRandomComment {
    private String title;
    private List<ModelStoresRandomComment> body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ModelStoresRandomComment> getBody() {
        return body;
    }

    public void setBody(List<ModelStoresRandomComment> body) {
        this.body = body;
    }
}
