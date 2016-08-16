package com.fanwe.seller.model.getGroupBuyCollect;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultGroupBuyCollect {

    private List<ModelGroupBuyCollect> body;

    private String title;

    public void setBody(List<ModelGroupBuyCollect> body) {
        this.body = body;
    }

    public List<ModelGroupBuyCollect> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
