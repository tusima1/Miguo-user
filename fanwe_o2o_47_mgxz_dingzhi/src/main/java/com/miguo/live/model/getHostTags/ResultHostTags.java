package com.miguo.live.model.getHostTags;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultHostTags {
    private String title;
    private List<ModelHostTags> body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ModelHostTags> getBody() {
        return body;
    }

    public void setBody(List<ModelHostTags> body) {
        this.body = body;
    }
}
