package com.miguo.live.model.getHostAuthTime;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultHostAuthTime {
    private String title;
    private List<ModelHostAuthTime> body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ModelHostAuthTime> getBody() {
        return body;
    }

    public void setBody(List<ModelHostAuthTime> body) {
        this.body = body;
    }
}
