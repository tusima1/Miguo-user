package com.fanwe.user.model.getAttentionFans;

import java.util.List;

/**
 * Created by didik on 2016/9/18.
 */
public class ResultFans {
    private String page_total;
    private String page;
    private String title;
    private String page_count;
    private String page_size;
    /**
     * nick : 19999999995
     * attention_status : 3
     * fansid : 420e0dac-7b37-4dd0-b04e-fc45cfeab65e
     * personality :
     * fans_user_id : e37ea905-b039-4453-8767-cdc0cb9c4141
     * fx_level : 1
     * icon :
     */

    private List<ModelFans> body;

    public String getPage_total() {
        return page_total;
    }

    public void setPage_total(String page_total) {
        this.page_total = page_total;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPage_count() {
        return page_count;
    }

    public void setPage_count(String page_count) {
        this.page_count = page_count;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public List<ModelFans> getBody() {
        return body;
    }

    public void setBody(List<ModelFans> body) {
        this.body = body;
    }
}
