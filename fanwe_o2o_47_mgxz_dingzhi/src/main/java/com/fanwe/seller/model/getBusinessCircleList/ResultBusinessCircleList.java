package com.fanwe.seller.model.getBusinessCircleList;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultBusinessCircleList {

    private List<ModelBusinessCircleList> body;

    private String title;

    public void setBody(List<ModelBusinessCircleList> body) {
        this.body = body;
    }

    public List<ModelBusinessCircleList> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
