package com.fanwe.seller.model.getClassifyList;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultClassifyList {

    private List<ModelClassifyList> body;

    private String title;

    public void setBody(List<ModelClassifyList> body) {
        this.body = body;
    }

    public List<ModelClassifyList> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
