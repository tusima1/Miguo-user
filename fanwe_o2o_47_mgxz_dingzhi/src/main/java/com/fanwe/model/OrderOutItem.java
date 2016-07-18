package com.fanwe.model;

import java.util.List;

/**
 * 
 * @author didikee
 * 
 * 外层
 *
 */
public class OrderOutItem {
	
//	'id': '164',
//    'order_sn': '2016020206300523',
//    'order_status': '0',
//    'pay_status': '2',
//    'payment_id': '0', //0,15余额支付; 16,17,20,21支付宝; 23,25微信；
//    'refund_status': '0',
//    'create_time': '2016-02-02 18:30:05',
//    'pay_amount': 14.5, //已支付总额
//    'total_price': 14.5, //订单总额
//    'c': 1, //包含商品总数
//    'deal_order_item': [
//	'status': '已支付',
//    'status_value': 2 //0:未支付,1部分支付,2:已支付,3:退款中,4:已退款,5:已完结,6:已取消
	
	private String id;
	private String order_sn;
	private String order_status;
	private String pay_status;
	private String payment_id;
	private String refund_status;
	private String create_time;
	private String pay_amount;
	private String total_price;
	private String c;
	private String status;
	private int status_value;
	private List<OrderInItem> deal_order_item;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getStatus_value() {
		return status_value;
	}
	public void setStatus_value(int status_value) {
		this.status_value = status_value;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getPay_status() {
		return pay_status;
	}
	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}
	public String getPayment_id() {
		return payment_id;
	}
	public void setPayment_id(String payment_id) {
		this.payment_id = payment_id;
	}
	public String getRefund_status() {
		return refund_status;
	}
	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getPay_amount() {
		return pay_amount;
	}
	public void setPay_amount(String pay_amount) {
		this.pay_amount = pay_amount;
	}
	public String getTotal_price() {
		return total_price;
	}
	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public List<OrderInItem> getDeal_order_item() {
		return deal_order_item;
	}
	public void setDeal_order_item(List<OrderInItem> deal_order_item) {
		this.deal_order_item = deal_order_item;
	}
	
	

}
