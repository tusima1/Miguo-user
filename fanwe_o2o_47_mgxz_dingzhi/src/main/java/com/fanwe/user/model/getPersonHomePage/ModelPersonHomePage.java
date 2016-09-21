package com.fanwe.user.model.getPersonHomePage;

/**
 * Created by didik on 2016/8/18.
 */
public class ModelPersonHomePage {
    private String nick;

    private String personality;

    private String icon;

    private String love_count;

    private String focus;

    private String fans;

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return this.nick;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getPersonality() {
        return this.personality;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setLove_count(String love_count) {
        this.love_count = love_count;
    }

    public String getLove_count() {
        return this.love_count;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public String getFocus() {
        return this.focus;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getFans() {
        return this.fans;
    }

}
