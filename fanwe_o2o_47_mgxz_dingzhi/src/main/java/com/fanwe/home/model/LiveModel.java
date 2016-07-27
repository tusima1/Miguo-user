package com.fanwe.home.model;

import java.util.List;

/**
 * 请求直播列表model
 * Created by Administrator on 2016/7/27.
 */
public class LiveModel {
    private int totalItem;

    private List<Room> recordList;

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public int getTotalItem() {
        return this.totalItem;
    }

    public void setRecordList(List<Room> recordList) {
        this.recordList = recordList;
    }

    public List<Room> getRecordList() {
        return this.recordList;
    }
}
