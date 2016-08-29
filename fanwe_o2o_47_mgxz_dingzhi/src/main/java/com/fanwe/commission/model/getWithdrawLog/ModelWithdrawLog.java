package com.fanwe.commission.model.getWithdrawLog;

/**
 * Created by didik on 2016/8/29.
 */
public class ModelWithdrawLog {
    /**
     * bank_card : 银行账号
     * money : 105 (金额)
     * bank_user : 开户人姓名
     * wd_time : 提现时间
     * user_id :  //用户id
     * wd_status : 1 //提现状态，1未提现，2提现成功，3提现失败',
     * bank_name : 开户行名称
     * apply_time : 1472117719024 申请时间
     * id : //
     * remarks ://备注
     * wd_to_where : 1 //0表示 提现至银行卡，1 表示 到余额'
     * wd_type : 提现类型，1:余额提现，2:佣金提现'
     */
    private String bank_card;
    private String money;
    private String bank_user;
    private String wd_time;
    private String user_id;
    private String wd_status;
    private String bank_name;
    private String apply_time;
    private String id;
    private String remarks;
    private String wd_to_where;
    private String wd_type;

    public String getWd_type() {
        return wd_type;
    }

    public void setWd_type(String wd_type) {
        this.wd_type = wd_type;
    }

    public String getBank_card() {
        return bank_card;
    }

    public void setBank_card(String bank_card) {
        this.bank_card = bank_card;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBank_user() {
        return bank_user;
    }

    public void setBank_user(String bank_user) {
        this.bank_user = bank_user;
    }

    public String getWd_time() {
        return wd_time;
    }

    public void setWd_time(String wd_time) {
        this.wd_time = wd_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getWd_status() {
        return wd_status;
    }

    public void setWd_status(String wd_status) {
        this.wd_status = wd_status;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getWd_to_where() {
        return wd_to_where;
    }

    public void setWd_to_where(String wd_to_where) {
        this.wd_to_where = wd_to_where;
    }
}
