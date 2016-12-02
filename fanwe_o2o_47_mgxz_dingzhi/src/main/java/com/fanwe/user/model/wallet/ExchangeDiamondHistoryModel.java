package com.fanwe.user.model.wallet;

import android.text.TextUtils;
import android.text.format.DateFormat;

import com.fanwe.utils.DataFormat;

import java.io.Serializable;

/**
 * 兑换果钻记录实体类.
 * Created by zhouhy on 2016/11/24.
 */

public class ExchangeDiamondHistoryModel implements Serializable {
    private String diamond;

    private String user_id;

    private String insert_time;

    private String id;

    private String bean;
    private int type=0;

    private String year_month;
    private String month_date;
    private String time_str;

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }

    public String getDiamond() {
        return this.diamond;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public String getInsert_time() {
        return this.insert_time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getBean() {
        return this.bean;
    }

    public String getYear_month() {
       return year_month;
    }
    public String  getMonth_date(){
        return month_date;
    }

    public String getTime_str() {
       return time_str;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTime_str(String time_str) {
        this.time_str = time_str;
    }

    public void setMonth_date(String month_date) {
        this.month_date = month_date;
    }

    public void setYear_month(String year_month) {
        this.year_month = year_month;
    }
}
