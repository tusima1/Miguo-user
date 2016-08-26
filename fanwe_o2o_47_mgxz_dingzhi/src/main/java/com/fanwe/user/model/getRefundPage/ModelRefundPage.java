package com.fanwe.user.model.getRefundPage;

/**
 * Created by didik on 2016/8/26.
 */
public class ModelRefundPage {

    private String balance_unit_price;// 退款单价
    private String sub_name;// 商品名称
    private String create_time;// 下单时间
    private String total_price;// 订单总价
    private String refunding;// 退款中
    private String pay_amount;// 已支付
    private String maxrefundnumber;// 退款最大件数
    private String number;// 商品数量
    private String refund_unit_price;// 结算单价
    private String name;// 商品名称
    private String consume_count;// 已使用
    private String refunded; // 已退款
    private String order_sn;// 订单编号

    public String getBalance_unit_price() {
        return balance_unit_price;
    }

    public void setBalance_unit_price(String balance_unit_price) {
        this.balance_unit_price = balance_unit_price;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getRefunding() {
        return refunding;
    }

    public void setRefunding(String refunding) {
        this.refunding = refunding;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getMaxrefundnumber() {
        return maxrefundnumber;
    }

    public void setMaxrefundnumber(String maxrefundnumber) {
        this.maxrefundnumber = maxrefundnumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRefund_unit_price() {
        return refund_unit_price;
    }

    public void setRefund_unit_price(String refund_unit_price) {
        this.refund_unit_price = refund_unit_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConsume_count() {
        return consume_count;
    }

    public void setConsume_count(String consume_count) {
        this.consume_count = consume_count;
    }

    public String getRefunded() {
        return refunded;
    }

    public void setRefunded(String refunded) {
        this.refunded = refunded;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }
}
