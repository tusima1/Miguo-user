package com.fanwe.user.model.getAttentionFans;

/**
 * Created by didik on 2016/9/18.
 */
public class ModelFans {
    private String nick;
    private String attention_status;
    private String fansid;
    private String personality;
    private String fans_user_id;
    private String fx_level;
    private String icon;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAttention_status() {
        return attention_status;
    }

    public void setAttention_status(String attention_status) {
        this.attention_status = attention_status;
    }

    public String getFansid() {
        return fansid;
    }

    public void setFansid(String fansid) {
        this.fansid = fansid;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getFans_user_id() {
        return fans_user_id;
    }

    public void setFans_user_id(String fans_user_id) {
        this.fans_user_id = fans_user_id;
    }

    public String getFx_level() {
        return fx_level;
    }

    public void setFx_level(String fx_level) {
        this.fx_level = fx_level;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
