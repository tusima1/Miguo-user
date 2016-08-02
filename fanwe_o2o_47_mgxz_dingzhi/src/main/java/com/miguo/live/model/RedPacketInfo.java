package com.miguo.live.model;

/**
 * 红包类型实体类。
 * Created by Administrator on 2016/8/1.
 */
public class RedPacketInfo {

    /**
     * 红包类型。
     */
    public int type;
    /**
     * 红包剩余数量。
     */
    public int count;
    /**
     * 是否可以点击。
     */
    public boolean clickable = true;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}
