package com.fanwe.user.model.postUserUpgradeOrder;

/**
 * Created by Administrator on 2016/8/29.
 */
public class Config {
    private String payment_type;

    private String out_trade_no;

    private String partner;

    private String _input_charset;

    private String service;

    private String subject;

    private String total_fee;

    private String sign;

    private String body;

    private String notify_url;

    private String sign_type;

    private String seller_id;

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getPayment_type() {
        return this.payment_type;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOut_trade_no() {
        return this.out_trade_no;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getPartner() {
        return this.partner;
    }

    public void set_input_charset(String _input_charset) {
        this._input_charset = _input_charset;
    }

    public String get_input_charset() {
        return this._input_charset;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getService() {
        return this.service;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getTotal_fee() {
        return this.total_fee;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return this.sign;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getNotify_url() {
        return this.notify_url;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign_type() {
        return this.sign_type;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_id() {
        return this.seller_id;
    }
}
