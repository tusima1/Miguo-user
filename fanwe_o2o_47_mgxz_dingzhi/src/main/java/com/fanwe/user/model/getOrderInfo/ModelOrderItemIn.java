package com.fanwe.user.model.getOrderInfo;

/**
 * Created by didik on 2016/8/23.
 */
public class ModelOrderItemIn {
    private String number;//数量
    private String refund_status;//退款状态 0:无 1:用户申请退款 2:已确认 3:拒绝退款
    private String buss_name;//商户名称
    private String total_price;//团购总价
    private String tuan_id;//团购id
    private String name;//团购名称
    private String consume_count;//成功消费的商品数量(已验证/已收货/已付款)
    private String icon;//团购小图
    private String detail_id;//订单明细id
    private String order_sn;//订单号
    private String cate_id;//分类icon
    private String dp_id;//点评
    private String is_refund;//是否支持退款，０，无；　１：用户申请退款；　２：已确认；　３：拒绝退款
    private String refunded;//已退款
    private String refunding;//正在退款
    private String can_refund;//是否可退款：0：不可退款，1：可退款
    private String can_comment;//是否可评价：0：不可评价，1：可评价
    private String can_consume;//是否可消费：0，不可消费，1，可消费

    public String getCan_refund() {
        return can_refund;
    }

    public void setCan_refund(String can_refund) {
        this.can_refund = can_refund;
    }

    public String getCan_comment() {
        return can_comment;
    }

    public void setCan_comment(String can_comment) {
        this.can_comment = can_comment;
    }

    public String getCan_consume() {
        return can_consume;
    }

    public void setCan_consume(String can_consume) {
        this.can_consume = can_consume;
    }

    public String getRefunded() {
        return refunded;
    }

    public void setRefunded(String refunded) {
        this.refunded = refunded;
    }

    public String getRefunding() {
        return refunding;
    }

    public void setRefunding(String refunding) {
        this.refunding = refunding;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getIs_refund() {
        return is_refund;
    }

    public void setIs_refund(String is_refund) {
        this.is_refund = is_refund;
    }

    public String getDp_id() {
        return dp_id;
    }

    public void setDp_id(String dp_id) {
        this.dp_id = dp_id;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    public String getBuss_name() {
        return buss_name;
    }

    public void setBuss_name(String buss_name) {
        this.buss_name = buss_name;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getTuan_id() {
        return tuan_id;
    }

    public void setTuan_id(String tuan_id) {
        this.tuan_id = tuan_id;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDetail_id() {
        return detail_id;
    }

    public void setDetail_id(String detail_id) {
        this.detail_id = detail_id;
    }
}
