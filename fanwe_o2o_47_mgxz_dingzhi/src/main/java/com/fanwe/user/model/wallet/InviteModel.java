package com.fanwe.user.model.wallet;

import java.io.Serializable;

/**
 * 邀请奖金实体类。
 * Created by zhouhy on 2016/11/30.
 */

public class InviteModel implements Serializable {
    /**
     *描述。
     */
    public String description;
    /**
     * 金额。
     */
    public String money;
    /**
     * 插入时间。
     */
    public String insert_time;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
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
