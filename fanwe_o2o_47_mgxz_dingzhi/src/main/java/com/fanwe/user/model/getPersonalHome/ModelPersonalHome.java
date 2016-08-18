package com.fanwe.user.model.getPersonalHome;

/**
 * Created by didik on 2016/8/18.
 */
public class ModelPersonalHome {


    /*
    "pending_pay":"0",//待付款数
    "now_user_points":"0",//总积分
    "total_user_commission":"88.00",//总佣金
    "pending_evaluation":"0",//待评价
    "icon":"http://img.jgzyw.com:8000/d/img/touxiang/2015/01/01/201501010824004721.jpg",//头像
    "user_message":"0",//消息
    "coupons_count":"0",//消费券
    "withdrawals":"66.00",//可提现佣金
    "refunt":"0",//退款数
    "nick":"蜗牛",//昵称
    "red_packet_count":"0",//拥有红包数
    "evaluation":"0",//总评论数
    "forecast_estimated_money":"136.00",//预计可提现佣金
    "fans_count":"0",粉丝数
    "now_user_account_money":"77.00",//余额
    "new_red_packet":"0",//判断是否存在未查看红包0：不存在，1存在
    "use_money":22,//已使用佣金
    "collect":"0",//收藏数（当前版本可能不实装收藏，可以无视）
    "pending_use":"0"//待使用
    */

    private String pending_pay;
    private String now_user_points;
    private String total_user_commission;
    private String pending_evaluation;
    private String icon;
    private String user_message;
    private String coupons_count;
    private String withdrawals;
    private String refunt;
    private String nick;
    private String red_packet_count;
    private String evaluation;
    private String forecast_estimated_money;
    private String fans_count;
    private String now_user_account_money;
    private String new_red_packet;
    private int use_money;
    private String collect;
    private String pending_use;

    public String getPending_pay() {
        return pending_pay;
    }

    public void setPending_pay(String pending_pay) {
        this.pending_pay = pending_pay;
    }

    public String getNow_user_points() {
        return now_user_points;
    }

    public void setNow_user_points(String now_user_points) {
        this.now_user_points = now_user_points;
    }

    public String getTotal_user_commission() {
        return total_user_commission;
    }

    public void setTotal_user_commission(String total_user_commission) {
        this.total_user_commission = total_user_commission;
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

    public String getUser_message() {
        return user_message;
    }

    public void setUser_message(String user_message) {
        this.user_message = user_message;
    }

    public String getCoupons_count() {
        return coupons_count;
    }

    public void setCoupons_count(String coupons_count) {
        this.coupons_count = coupons_count;
    }

    public String getWithdrawals() {
        return withdrawals;
    }

    public void setWithdrawals(String withdrawals) {
        this.withdrawals = withdrawals;
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

    public String getRed_packet_count() {
        return red_packet_count;
    }

    public void setRed_packet_count(String red_packet_count) {
        this.red_packet_count = red_packet_count;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getForecast_estimated_money() {
        return forecast_estimated_money;
    }

    public void setForecast_estimated_money(String forecast_estimated_money) {
        this.forecast_estimated_money = forecast_estimated_money;
    }

    public String getFans_count() {
        return fans_count;
    }

    public void setFans_count(String fans_count) {
        this.fans_count = fans_count;
    }

    public String getNow_user_account_money() {
        return now_user_account_money;
    }

    public void setNow_user_account_money(String now_user_account_money) {
        this.now_user_account_money = now_user_account_money;
    }

    public String getNew_red_packet() {
        return new_red_packet;
    }

    public void setNew_red_packet(String new_red_packet) {
        this.new_red_packet = new_red_packet;
    }

    public int getUse_money() {
        return use_money;
    }

    public void setUse_money(int use_money) {
        this.use_money = use_money;
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

}
