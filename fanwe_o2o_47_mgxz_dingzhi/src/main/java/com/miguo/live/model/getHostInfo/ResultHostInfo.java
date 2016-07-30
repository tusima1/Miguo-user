package com.miguo.live.model.getHostInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultHostInfo {
    private String title;
    private List<ModelHostInfo> body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ModelHostInfo> getBody() {
        return body;
    }

    public void setBody(List<ModelHostInfo> body) {
        this.body = body;
    }
}
