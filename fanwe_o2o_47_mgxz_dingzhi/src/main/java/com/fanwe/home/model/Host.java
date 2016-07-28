package com.fanwe.home.model;

import java.util.List;

/**
 * 主播
 * Created by Administrator on 2016/7/27.
 */
public class Host {

    private String uid;

    private String nickname;

    private List<String> tags;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
