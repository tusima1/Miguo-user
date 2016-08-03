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
    /**
     * 主播用户id. userid
     */
    private String host_user_id;

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

    public String getHost_user_id() {
        return host_user_id;
    }

    public void setHost_user_id(String host_user_id) {
        this.host_user_id = host_user_id;
    }
}
