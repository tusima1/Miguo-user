package com.fanwe.user.model.getRefundPage;

import java.util.List;

/**
 * Created by didik on 2016/8/26.
 */
public class ResultRefundPage {
    private String title;
    private List<ModelRefundPage> body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ModelRefundPage> getBody() {
        return body;
    }

    public void setBody(List<ModelRefundPage> body) {
        this.body = body;
    }
}
