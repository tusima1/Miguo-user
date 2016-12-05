package com.fanwe.seller.model.getShopMemberInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultShopMemberInfo {

    private List<ModelShopMemberInfo> body;

    private String title;

    public void setBody(List<ModelShopMemberInfo> body) {
        this.body = body;
    }

    public List<ModelShopMemberInfo> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
