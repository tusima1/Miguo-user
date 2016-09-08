package com.fanwe.user.model;

import java.io.Serializable;

/**
 * 第三方登录用户信息。
 * Created by Administrator on 2016/8/31.
 */
public class ThirdLoginInfo implements Serializable {
    public String platformType;
    public String icon;
    public String nick;
    public String openId;

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
