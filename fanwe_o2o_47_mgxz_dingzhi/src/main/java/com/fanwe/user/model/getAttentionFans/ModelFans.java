package com.fanwe.user.model.getAttentionFans;

/**
 * Created by didik on 2016/9/18.
 */
public class ModelFans {
//    "nick":"谷俊山",//粉丝昵称
//            "attention_status":"1",//状态：1：未关注 2：已关注 3：互相关注
//            "fansid":"5b143a5a-7129-4823-a8d9-a15255d78f31",
//            "personality":"",//个性签名
//            "fans_user_id":"602e1285-0948-4dbd-ba10-ad3d6710eee3",//粉丝的用户id
//            "fx_level":"3",//分销等级，应用于列表昵称旁的图标
//            "icon":"http://obc58vgro.bkt.clouddn.com/2016/09/Fh1TTwGfMJbeX7lMSj6EHBOoHG6x"//头像
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
