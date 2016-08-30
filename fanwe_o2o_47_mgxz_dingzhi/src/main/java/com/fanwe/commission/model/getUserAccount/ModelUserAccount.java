package com.fanwe.commission.model.getUserAccount;

/**
 * Created by didik on 2016/8/29.
 */
public class ModelUserAccount {
    /*user_id` varchar(64) NOT NULL,
    `card_type` varchar(64) DEFAULT NULL COMMENT '证件类型',
    `card_id` varchar(50) DEFAULT NULL COMMENT '证件号码',
    `qq` varchar(50) DEFAULT NULL,
    `wx_openid` varchar(50) DEFAULT NULL,
    `tb_openid` varchar(50) DEFAULT NULL,
    `yx_openid` varchar(50) DEFAULT NULL,
    `tel` varchar(50) DEFAULT NULL COMMENT '电话',
    `phone` varchar(50) DEFAULT NULL COMMENT '手机',
    `email` varchar(50) DEFAULT NULL,
    `home_address` varchar(100) DEFAULT NULL COMMENT '家庭住址',
    `home_code` varchar(50) DEFAULT NULL COMMENT '邮编',
    `emer_contact` varchar(50) DEFAULT NULL COMMENT '紧急联系人',
    `emer_phone` varchar(50) DEFAULT NULL COMMENT '紧急联系人电话',
    `bank_type` varchar(64) DEFAULT NULL COMMENT '银行卡类型',
    `bank_name` varchar(50) DEFAULT NULL COMMENT '开户行名称',
    `bank_unionid` varchar(50) DEFAULT NULL COMMENT '银行联行号',
    `bank_card` varchar(50) DEFAULT NULL COMMENT '银行卡号',
    `bank_code` varchar(50) DEFAULT NULL COMMENT '银行卡识别号',
    `bank_phone` varchar(50) DEFAULT NULL COMMENT '银行预留手机号',
    `level_value` int(11) DEFAULT NULL COMMENT '信用值',
    `level_id` int(11) DEFAULT NULL COMMENT '用户等级',
    `point` int(11) DEFAULT NULL COMMENT '用户实际积分',
    `all_point` int(11) DEFAULT NULL COMMENT '累计积分',
    `money` decimal(20,2) DEFAULT NULL COMMENT '金额',
    `tencent_id` varchar(50) DEFAULT NULL COMMENT '腾讯微博ID',
    `sina_id` varchar(50) DEFAULT NULL COMMENT '新浪同步的会员ID',
    `sina_app_key` varchar(50) DEFAULT NULL COMMENT '新浪的同步验证key',
    `sina_app_secret` varchar(50) DEFAULT NULL COMMENT '新浪的同步验证密码',
    `is_syn_sina` int(11) DEFAULT NULL COMMENT '是否同步发微博到新浪',
    `tencent_app_key` varchar(50) DEFAULT NULL COMMENT '腾讯的同步验证key',
    `tencent_app_secret` varchar(50) DEFAULT NULL COMMENT '腾讯的同步验证密码',
    `is_syn_tencent` int(11) DEFAULT NULL COMMENT '是否同步发微博到腾讯',
    `sina_token` varchar(50) DEFAULT NULL COMMENT '新浪的授权码',
    `tencent_token` varchar(50) DEFAULT NULL COMMENT '腾讯微博授权码',
    `bank_user` varchar(50) DEFAULT NULL COMMENT '开户行户名',*/

    private String bank_card;
    private String sina_token;
    private String sina_id;
    private String emer_contact;
    private String level_id;
    private String tencent_app_secret;
    private String point;
    private String sina_app_key;
    private String wx_openid;
    private String bank_user;
    private String home_address;
    private String bank_phone;
    private String bank_name;
    private String tencent_token;
    private String tel;
    private String is_syn_sina;
    private String yx_openid;
    private String email;
    private String qq;
    private String bank_code;
    private String bank_type;
    private String is_syn_tencent;
    private String card_type;
    private String bank_unionid;
    private String card_id;
    private String sina_app_secret;
    private String home_code;
    private String money;
    private String user_id;
    private String phone;
    private String emer_phone;
    private String level_value;
    private String all_point;
    private String tencent_id;
    private String tencent_app_key;
    private String tb_openid;

    public String getBank_card() {
        return bank_card;
    }

    public void setBank_card(String bank_card) {
        this.bank_card = bank_card;
    }

    public String getSina_token() {
        return sina_token;
    }

    public void setSina_token(String sina_token) {
        this.sina_token = sina_token;
    }

    public String getSina_id() {
        return sina_id;
    }

    public void setSina_id(String sina_id) {
        this.sina_id = sina_id;
    }

    public String getEmer_contact() {
        return emer_contact;
    }

    public void setEmer_contact(String emer_contact) {
        this.emer_contact = emer_contact;
    }

    public String getLevel_id() {
        return level_id;
    }

    public void setLevel_id(String level_id) {
        this.level_id = level_id;
    }

    public String getTencent_app_secret() {
        return tencent_app_secret;
    }

    public void setTencent_app_secret(String tencent_app_secret) {
        this.tencent_app_secret = tencent_app_secret;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getSina_app_key() {
        return sina_app_key;
    }

    public void setSina_app_key(String sina_app_key) {
        this.sina_app_key = sina_app_key;
    }

    public String getWx_openid() {
        return wx_openid;
    }

    public void setWx_openid(String wx_openid) {
        this.wx_openid = wx_openid;
    }

    public String getBank_user() {
        return bank_user;
    }

    public void setBank_user(String bank_user) {
        this.bank_user = bank_user;
    }

    public String getHome_address() {
        return home_address;
    }

    public void setHome_address(String home_address) {
        this.home_address = home_address;
    }

    public String getBank_phone() {
        return bank_phone;
    }

    public void setBank_phone(String bank_phone) {
        this.bank_phone = bank_phone;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getTencent_token() {
        return tencent_token;
    }

    public void setTencent_token(String tencent_token) {
        this.tencent_token = tencent_token;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIs_syn_sina() {
        return is_syn_sina;
    }

    public void setIs_syn_sina(String is_syn_sina) {
        this.is_syn_sina = is_syn_sina;
    }

    public String getYx_openid() {
        return yx_openid;
    }

    public void setYx_openid(String yx_openid) {
        this.yx_openid = yx_openid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public String getIs_syn_tencent() {
        return is_syn_tencent;
    }

    public void setIs_syn_tencent(String is_syn_tencent) {
        this.is_syn_tencent = is_syn_tencent;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getBank_unionid() {
        return bank_unionid;
    }

    public void setBank_unionid(String bank_unionid) {
        this.bank_unionid = bank_unionid;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getSina_app_secret() {
        return sina_app_secret;
    }

    public void setSina_app_secret(String sina_app_secret) {
        this.sina_app_secret = sina_app_secret;
    }

    public String getHome_code() {
        return home_code;
    }

    public void setHome_code(String home_code) {
        this.home_code = home_code;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmer_phone() {
        return emer_phone;
    }

    public void setEmer_phone(String emer_phone) {
        this.emer_phone = emer_phone;
    }

    public String getLevel_value() {
        return level_value;
    }

    public void setLevel_value(String level_value) {
        this.level_value = level_value;
    }

    public String getAll_point() {
        return all_point;
    }

    public void setAll_point(String all_point) {
        this.all_point = all_point;
    }

    public String getTencent_id() {
        return tencent_id;
    }

    public void setTencent_id(String tencent_id) {
        this.tencent_id = tencent_id;
    }

    public String getTencent_app_key() {
        return tencent_app_key;
    }

    public void setTencent_app_key(String tencent_app_key) {
        this.tencent_app_key = tencent_app_key;
    }

    public String getTb_openid() {
        return tb_openid;
    }

    public void setTb_openid(String tb_openid) {
        this.tb_openid = tb_openid;
    }
}
