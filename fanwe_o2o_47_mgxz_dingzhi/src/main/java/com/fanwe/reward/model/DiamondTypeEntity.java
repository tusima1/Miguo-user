package com.fanwe.reward.model;

import java.io.Serializable;

/**
 * 果钻充值类型。
 * Created by Administrator on 2016/9/11.
 */
public class DiamondTypeEntity implements Serializable {
    /**
     * 钻数.
     */
    private String diamond;
    /**
     * 价格。
     */
    private String price;
    /**
     * ID.
     */

    private String id;

    private String is_delete;
    private boolean isChecked;

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }

    public String getDiamond() {
        return this.diamond;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return this.price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getIs_delete() {
        return this.is_delete;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
