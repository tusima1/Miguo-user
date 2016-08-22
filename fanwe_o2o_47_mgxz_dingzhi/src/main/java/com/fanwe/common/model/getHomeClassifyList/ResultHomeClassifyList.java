package com.fanwe.common.model.getHomeClassifyList;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultHomeClassifyList {

    private List<ModelHomeClassifyList> body;

    private String title;

    public void setBody(List<ModelHomeClassifyList> body) {
        this.body = body;
    }

    public List<ModelHomeClassifyList> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
