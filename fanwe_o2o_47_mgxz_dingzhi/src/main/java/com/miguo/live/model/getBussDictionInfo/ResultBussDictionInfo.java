package com.miguo.live.model.getBussDictionInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultBussDictionInfo {
    private String title;
    private List<ModelBussDictionInfo> body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ModelBussDictionInfo> getBody() {
        return body;
    }

    public void setBody(List<ModelBussDictionInfo> body) {
        this.body = body;
    }
}
