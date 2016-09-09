package com.fanwe.commission.model.getUserBankCardList;

import java.util.List;

/**
 * Created by didik on 2016/9/9.
 */
public class ResultUserBankCard {
    private String title;
    private List<ModelUserBankCard> body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ModelUserBankCard> getBody() {
        return body;
    }

    public void setBody(List<ModelUserBankCard> body) {
        this.body = body;
    }
}
