package com.fanwe.seller.model.getStoreList;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultStoreList {
    private String page_total;

    private String page;

    private List<ModelStoreList> body;

    private String title;

    private String page_count;

    private String page_size;

    public void setPage_total(String page_total) {
        this.page_total = page_total;
    }

    public String getPage_total() {
        return this.page_total;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage() {
        return this.page;
    }

    public List<ModelStoreList> getBody() {
        return body;
    }

    public void setBody(List<ModelStoreList> body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setPage_count(String page_count) {
        this.page_count = page_count;
    }

    public String getPage_count() {
        return this.page_count;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public String getPage_size() {
        return this.page_size;
    }

}
