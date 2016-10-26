package com.miguo.live.model.getAudienceList;

/**
 * 观众实体类。
 * Created by Administrator on 2016/7/30.
 */
public class ModelAudienceInfo {

    /**
     * 头像。
     */
    private String icon;
    /**
     * 用户ID
     */

    private String user_id;
    /**
     *  呢称。
     */

    private String nick;

    //机器人头像
    private int iconRes;

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String toString() {
        return "ModelAudienceList{" +
                "icon='" + icon + '\'' +
                ", nick='" + nick + '\'' +
                '}';
    }
}
