package com.fanwe.commission.model.getUserAccount;

import java.util.List;

/**
 * Created by didik on 2016/8/29.
 */
public class ResultUserAccount {
    private String title;


    private List<ModelUserAccount> body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ModelUserAccount> getBody() {
        return body;
    }

    public void setBody(List<ModelUserAccount> body) {
        this.body = body;
    }
}
