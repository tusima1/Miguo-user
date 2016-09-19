package com.fanwe.home.model;

import android.text.TextUtils;

import java.util.List;

/**
 * 主播
 * Created by Administrator on 2016/7/27.
 */
public class Host {

    private String uid;

    private String nickname;

    private List<String> tags;
    private String avatar;
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
        /*校验nick长度*/
        if (TextUtils.isEmpty(nickname)){
            return nickname;
        }
        int length = nickname.length();
        if (length<=10){
            return nickname;
        }else {
            String newNick=nickname.substring(0,8);
            return newNick+"...";
        }
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
