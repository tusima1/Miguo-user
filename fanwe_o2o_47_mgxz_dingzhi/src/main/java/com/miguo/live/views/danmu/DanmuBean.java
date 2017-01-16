package com.miguo.live.views.danmu;

import java.io.Serializable;

/**
 * Created by zlh on 2016/9/12.
 */
public class DanmuBean implements Serializable{

    int avatar;
    String content;
    String name;
    String url;

    public DanmuBean(int avatar, String content, String name) {
        this.avatar = avatar;
        this.content = content;
        this.name = name;
    }

    public DanmuBean(String avatar, String content, String name) {
        this.content = content;
        this.name = name;
        setUrl(avatar);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
