package com.fanwe.user.model.getDistrInfo;

import java.util.List;

/**
 * Created by didik on 2016/8/22.
 */
public class ResultDistrInfo {
    private List<ModelDistrInfo> body;

    private String title;

    public void setBody(List<ModelDistrInfo> body) {
        this.body = body;
    }

    public List<ModelDistrInfo> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
