package com.fanwe.shoppingcart.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/10.
 */
public class Share_info implements Serializable {
    private String clickurl;

    private String summary;

    private String salarySum;

    private String imageurl;

    private String title;

    public void setClickurl(String clickurl) {
        this.clickurl = clickurl;
    }

    public String getClickurl() {
        return this.clickurl;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSalarySum(String salarySum) {
        this.salarySum = salarySum;
    }

    public String getSalarySum() {
        return this.salarySum;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getImageurl() {
        return this.imageurl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
