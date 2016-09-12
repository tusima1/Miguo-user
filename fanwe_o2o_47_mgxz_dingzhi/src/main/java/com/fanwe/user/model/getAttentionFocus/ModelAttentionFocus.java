package com.fanwe.user.model.getAttentionFocus;

public class ModelAttentionFocus {
    private String nick;

    private String attention_status;

    private String personality;

    private String fx_level;

    private String icon;

    private String focusid;

    private String focus_user_id;

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return this.nick;
    }

    public void setAttention_status(String attention_status) {
        this.attention_status = attention_status;
    }

    public String getAttention_status() {
        return this.attention_status;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getPersonality() {
        return this.personality;
    }

    public void setFx_level(String fx_level) {
        this.fx_level = fx_level;
    }

    public String getFx_level() {
        return this.fx_level;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setFocusid(String focusid) {
        this.focusid = focusid;
    }

    public String getFocusid() {
        return this.focusid;
    }

    public void setFocus_user_id(String focus_user_id) {
        this.focus_user_id = focus_user_id;
    }

    public String getFocus_user_id() {
        return this.focus_user_id;
    }
}
