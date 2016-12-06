package com.miguo.entity;

import java.io.Serializable;

/**
 * banner跳转，根据json解析出来的数据不同处理。
 * Created by zhouhy on 2016/11/25.
 */

public class BannerTypeModel implements Serializable {
    public String id;
    public String url;
    public String tag;
    public String cate_id;
    public String tid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}
