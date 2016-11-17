package com.miguo.live.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/17.
 */

public class PageModel implements Serializable{
    /**
     * 总页数.
     */
    private String page_total;
    /**
     * 当前页码.
     */
    private String page;

    /**
     * 总记录数.
     */
    private String page_count;
    /**
     * 分页大小
     */
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
