package com.fanwe.seller.model.getCityList;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultCityList {

    private List<ModelCityList> body;

    private String title;

    public void setBody(List<ModelCityList> body) {
        this.body = body;
    }

    public List<ModelCityList> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
