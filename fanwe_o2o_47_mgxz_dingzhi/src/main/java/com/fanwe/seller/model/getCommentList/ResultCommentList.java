package com.fanwe.seller.model.getCommentList;

import com.fanwe.seller.model.ModelComment;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultCommentList {

    private List<ModelComment> body;

    private String title;

    public void setBody(List<ModelComment> body) {
        this.body = body;
    }

    public List<ModelComment> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
