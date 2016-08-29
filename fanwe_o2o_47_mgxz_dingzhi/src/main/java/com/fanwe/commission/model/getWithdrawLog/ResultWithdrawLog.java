package com.fanwe.commission.model.getWithdrawLog;

import java.util.List;

/**
 * Created by didik on 2016/8/29.
 */
public class ResultWithdrawLog {
    private String page_total;
    private String page;
    private String title;
    private String page_count;
    private String page_size;


    private List<ModelWithdrawLog> body;

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

    public List<ModelWithdrawLog> getBody() {
        return body;
    }

    public void setBody(List<ModelWithdrawLog> body) {
        this.body = body;
    }

}
