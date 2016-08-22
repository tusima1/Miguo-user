package com.fanwe.seller.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/22.
 */
public class ModelDisplayComment implements Serializable {
    private String message_count; // 点评数量
    private String pageTitle;
    private String star_1; // 1星人数
    private String star_2; // 2星人数
    private String star_3; // 3星人数
    private String star_4; // 4星人数
    private String star_5; // 5星人数
    private int star_dp_width_1;
    private int star_dp_width_2;
    private int star_dp_width_3;
    private int star_dp_width_4;
    private int star_dp_width_5;
    private String buy_dp_width; // 平均值 进度条长度
    private String buy_dp_sum; // 购买点评数量
    private String buy_dp_avg; // 点评平均值
    private int allow_dp; // 1:允许点评，0:不允许
    private String name;

    public String getMessage_count() {
        return message_count;
    }

    public void setMessage_count(String message_count) {
        this.message_count = message_count;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getStar_1() {
        return star_1;
    }

    public void setStar_1(String star_1) {
        this.star_1 = star_1;
    }

    public String getStar_2() {
        return star_2;
    }

    public void setStar_2(String star_2) {
        this.star_2 = star_2;
    }

    public String getStar_3() {
        return star_3;
    }

    public void setStar_3(String star_3) {
        this.star_3 = star_3;
    }

    public String getStar_4() {
        return star_4;
    }

    public void setStar_4(String star_4) {
        this.star_4 = star_4;
    }

    public String getStar_5() {
        return star_5;
    }

    public void setStar_5(String star_5) {
        this.star_5 = star_5;
    }

    public int getStar_dp_width_1() {
        if (TextUtils.isEmpty(message_count)) {
            return 0;
        }
        if (Integer.valueOf(message_count) == 0) {
            return 0;
        }
        return Integer.valueOf(star_1) * 100 / Integer.valueOf(message_count);
    }

    public void setStar_dp_width_1(int star_dp_width_1) {
        this.star_dp_width_1 = star_dp_width_1;
    }

    public int getStar_dp_width_2() {
        if (TextUtils.isEmpty(message_count)) {
            return 0;
        }
        if (Integer.valueOf(message_count) == 0) {
            return 0;
        }
        return Integer.valueOf(star_2) * 100 / Integer.valueOf(message_count);
    }

    public void setStar_dp_width_2(int star_dp_width_2) {
        this.star_dp_width_2 = star_dp_width_2;
    }

    public int getStar_dp_width_3() {
        if (TextUtils.isEmpty(message_count)) {
            return 0;
        }
        if (Integer.valueOf(message_count) == 0) {
            return 0;
        }
        return Integer.valueOf(star_3) * 100 / Integer.valueOf(message_count);
    }

    public void setStar_dp_width_3(int star_dp_width_3) {
        this.star_dp_width_3 = star_dp_width_3;
    }

    public int getStar_dp_width_4() {
        if (TextUtils.isEmpty(message_count)) {
            return 0;
        }
        if (Integer.valueOf(message_count) == 0) {
            return 0;
        }
        return Integer.valueOf(star_4) * 100 / Integer.valueOf(message_count);
    }

    public void setStar_dp_width_4(int star_dp_width_4) {
        this.star_dp_width_4 = star_dp_width_4;
    }

    public int getStar_dp_width_5() {
        if (TextUtils.isEmpty(message_count)) {
            return 0;
        }
        if (Integer.valueOf(message_count) == 0) {
            return 0;
        }
        return Integer.valueOf(star_5) * 100 / Integer.valueOf(message_count);
    }

    public void setStar_dp_width_5(int star_dp_width_5) {
        this.star_dp_width_5 = star_dp_width_5;
    }

    public String getBuy_dp_width() {
        return buy_dp_width;
    }

    public void setBuy_dp_width(String buy_dp_width) {
        this.buy_dp_width = buy_dp_width;
    }

    public String getBuy_dp_sum() {
        return buy_dp_sum;
    }

    public void setBuy_dp_sum(String buy_dp_sum) {
        this.buy_dp_sum = buy_dp_sum;
    }

    public String getBuy_dp_avg() {
        return buy_dp_avg;
    }

    public void setBuy_dp_avg(String buy_dp_avg) {
        this.buy_dp_avg = buy_dp_avg;
    }

    public int getAllow_dp() {
        return allow_dp;
    }

    public void setAllow_dp(int allow_dp) {
        this.allow_dp = allow_dp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
