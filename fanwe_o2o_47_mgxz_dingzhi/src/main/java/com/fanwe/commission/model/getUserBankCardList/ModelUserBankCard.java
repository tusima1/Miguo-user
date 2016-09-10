package com.fanwe.commission.model.getUserBankCardList;

/**
 * Created by didik on 2016/9/9.
 */
public class ModelUserBankCard {
    /**
     * bank_card : 1 '银行卡号'
     * bank_type : '银行类型(预留字段)'
     * bank_user : 2 '开户人姓名'
     * create_time : 1473327453642 '创建时间'
     * user_id : c847c55c-5804-4d48-a341-85c4ff9c2f2c '用户id'
     * is_enable : 1 //'是否有效 1有效0无效'
     * bank_name : 1 '开户行名字'
     * id : 50d3fbcc-6fb7-4814-b1fe-865a222ce9b0
     */
    private String bank_card;
    private String bank_type;
    private String bank_user;
    private String create_time;
    private String user_id;
    private String is_enable;
    private String bank_name;
    private String id;

    public String getBank_card() {
        return bank_card;
    }

    public void setBank_card(String bank_card) {
        this.bank_card = bank_card;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public String getBank_user() {
        return bank_user;
    }

    public void setBank_user(String bank_user) {
        this.bank_user = bank_user;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
