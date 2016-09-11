package com.miguo.live.model.payHistory;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultPayHistory {
    private String title;
    private List<ModelPayHistory> body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ModelPayHistory> getBody() {
        return body;
    }

    public void setBody(List<ModelPayHistory> body) {
        this.body = body;
    }
}
