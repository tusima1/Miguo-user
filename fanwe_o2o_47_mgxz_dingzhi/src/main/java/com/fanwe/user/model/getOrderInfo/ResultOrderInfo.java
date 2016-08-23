package com.fanwe.user.model.getOrderInfo;

import java.util.List;

/**
 * Created by didik on 2016/8/23.
 */
public class ResultOrderInfo {
    private String pageTitle;
    private String pageSize;
    private String page;
    private String dataNum;//每页数量
    /**
     * order_status : 0
     * create_time : 1470884540240
     * total_price : 1564.00
     * status_name : 未支付
     * pay_amount : 0.00
     * deal_order_item : [{"number":"3","refund_status":"0","buss_name":"嵊州生活网",
     * "total_price":"1564.00","tuan_id":"32b8820d-13f6-4f14-9c24-355dae972523",
     * "name":"保定1号团购1","consume_count":"0","icon":"",
     * "detail_id":"ca58198a-c444-4912-8194-93985ca8dfb2"}]
     * order_id : 905fadd8-1e72-4eaf-b577-7e43f2716e2a
     * order_type : 2
     * order_sn : 2016081111022030
     */


    private List<ModelOrderItemOut> items;

    public String getDataNum() {
        return dataNum;
    }

    public void setDataNum(String dataNum) {
        this.dataNum = dataNum;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<ModelOrderItemOut> getItems() {
        return items;
    }

    public void setItems(List<ModelOrderItemOut> items) {
        this.items = items;
    }

}
