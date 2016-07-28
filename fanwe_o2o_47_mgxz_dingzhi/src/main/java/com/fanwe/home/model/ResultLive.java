package com.fanwe.home.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultLive {
    private String page_total;

    private String page;

    private List<Room> body;

    private String title;

    private String page_count;

    private String page_size;

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

    public List<Room> getBody() {
        return body;
    }

    public void setBody(List<Room> body) {
        this.body = body;
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
}
