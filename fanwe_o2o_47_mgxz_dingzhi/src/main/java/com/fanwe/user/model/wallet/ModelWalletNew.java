package com.fanwe.user.model.wallet;

import java.io.Serializable;

/**
 * 钱包页面自定义实体类。
 * Created by zhouhy on 2016/11/14.
 */
public class ModelWalletNew implements Serializable {
    /**
     * 图标。
     */
    private int  icon;
    /**
     * 标题 。
     */
    private String title;
    /**
     * 值
     */
    private String value;
    /**
     * 单位。
     */
    private String unit;


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
