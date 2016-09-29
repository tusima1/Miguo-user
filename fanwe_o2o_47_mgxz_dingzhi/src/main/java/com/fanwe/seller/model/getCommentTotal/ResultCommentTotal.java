package com.fanwe.seller.model.getCommentTotal;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultCommentTotal {

    private List<ModelCommentTotal> body;

    private String title;

    public void setBody(List<ModelCommentTotal> body) {
        this.body = body;
    }

    public List<ModelCommentTotal> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
