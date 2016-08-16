package com.fanwe.seller.model.getMarketList;

/**
 * Created by Administrator on 2016/8/12.
 */
public class ModelMarketListPage {
    private String page_total;

    private String data_total;

    private String page;

    private String page_size;

    public void setPage_total(String page_total) {
        this.page_total = page_total;
    }

    public String getPage_total() {
        return this.page_total;
    }

    public void setData_total(String data_total) {
        this.data_total = data_total;
    }

    public String getData_total() {
        return this.data_total;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage() {
        return this.page;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public String getPage_size() {
        return this.page_size;
    }
}
