package com.fanwe.user.model.wallet;

import java.io.Serializable;

/**退款实体类。
 * Created by zhouhy on 2016/11/28.
 */

public class RefundModel implements Serializable {
    /**
     * 描述。
     */
    private String description;
    /**
     * 订单号。
     */
    private String order_sn;
    /**
     * 金额。
     */
    private String money;
    /**
     * 时间。
     */
    private String insert_time;
    private int type=0;

    private String year_month;
    private String month_date;
    private String time_str;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_sn() {
        return this.order_sn;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMoney() {
        return this.money;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public String getInsert_time() {
        return this.insert_time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getYear_month() {
        return year_month;
    }

    public void setYear_month(String year_month) {
        this.year_month = year_month;
    }

    public String getMonth_date() {
        return month_date;
    }

    public void setMonth_date(String month_date) {
        this.month_date = month_date;
    }

    public String getTime_str() {
        return time_str;
    }

    public void setTime_str(String time_str) {
        this.time_str = time_str;
    }
}

