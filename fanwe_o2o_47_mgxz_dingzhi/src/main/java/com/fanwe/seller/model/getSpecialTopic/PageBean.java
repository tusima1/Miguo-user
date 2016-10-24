package com.fanwe.seller.model.getSpecialTopic;

/**
 * Created by didik on 2016/10/18.
 */

public class PageBean {
    private String page_total;
    private String data_total;
    private String page;
    private String page_size;

    public String getPage_total() {
        return page_total;
    }

    public void setPage_total(String page_total) {
        this.page_total = page_total;
    }

    public String getData_total() {
        return data_total;
    }

    public void setData_total(String data_total) {
        this.data_total = data_total;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }
}
