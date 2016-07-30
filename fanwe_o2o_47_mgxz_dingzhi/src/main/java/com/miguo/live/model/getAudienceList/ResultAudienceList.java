package com.miguo.live.model.getAudienceList;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultAudienceList {
    private String title;
    private List<ModelAudienceList> body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ModelAudienceList> getBody() {
        return body;
    }

    public void setBody(List<ModelAudienceList> body) {
        this.body = body;
    }
}
