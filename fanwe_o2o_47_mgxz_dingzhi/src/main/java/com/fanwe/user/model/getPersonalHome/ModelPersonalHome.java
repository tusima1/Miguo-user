package com.fanwe.user.model.getPersonalHome;

/**
 * Created by didik on 2016/8/18.
 */
public class ModelPersonalHome {


//    "focus_count":"14",//关注数
//            "pending_pay":"8",//待支付
//            "cart_count":"0",//购物车数
//            "fx_count":0,//分销战队数
//            "pending_evaluation":"0",//待评价
//            "coupon_count":"14",//消费券数
//            "icon":"http://obc58vgro.bkt.clouddn.com/2016/09/Fh1TTwGfMJbeX7lMSj6EHBOoHG6x",//头像
//            "refunt":"0",//退款数
//            "nick":"谷俊山",//昵称
//            "fans_count":"14",//粉丝数
//            "personality":"",//个性签名
//            "fx_level":"1",//分销等级
//            "collect":"5",//收藏数
//            "pending_use":"23"//待使用

    private String focus_count;//关注数
    private String cart_count;//购物车数
    private String fx_count;//分销战队数
    private String personality;//个性签名
    private String coupon_count;//消费券数

    private String pending_pay;
    private String pending_evaluation;
    private String icon;
    private String refunt;
    private String nick;
    private String fans_count;
    private String collect;
    private String pending_use;
    private String fx_level;

    public String getFocus_count() {
        return focus_count;
    }

    public void setFocus_count(String focus_count) {
        this.focus_count = focus_count;
    }

    public String getCart_count() {
        return cart_count;
    }

    public void setCart_count(String cart_count) {
        this.cart_count = cart_count;
    }

    public String getFx_count() {
        return fx_count;
    }

    public void setFx_count(String fx_count) {
        this.fx_count = fx_count;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getCoupon_count() {
        return coupon_count;
    }

    public void setCoupon_count(String coupon_count) {
        this.coupon_count = coupon_count;
    }

    public String getPending_pay() {
        return pending_pay;
    }

    public void setPending_pay(String pending_pay) {
        this.pending_pay = pending_pay;
    }



    public String getPending_evaluation() {
        return pending_evaluation;
    }

    public void setPending_evaluation(String pending_evaluation) {
        this.pending_evaluation = pending_evaluation;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getRefunt() {
        return refunt;
    }

    public void setRefunt(String refunt) {
        this.refunt = refunt;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }


    public String getFans_count() {
        return fans_count;
    }

    public void setFans_count(String fans_count) {
        this.fans_count = fans_count;
    }


    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getPending_use() {
        return pending_use;
    }

    public void setPending_use(String pending_use) {
        this.pending_use = pending_use;
    }

    public String getFx_level() {
        return fx_level;
    }

    public void setFx_level(String fx_level) {
        this.fx_level = fx_level;
    }
}
