package com.fanwe.home.model;

/**
 * 主播
 * Created by Administrator on 2016/7/27.
 */
public class Host {
    private String uid;

    private String avatar;

    private String username;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return this.uid;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}
