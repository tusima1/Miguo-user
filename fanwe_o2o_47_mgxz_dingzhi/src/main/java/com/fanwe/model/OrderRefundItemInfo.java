package com.fanwe.model;

/**
 * 
 * @author didikee
 * 
 *         退款订单页面
 */
public class OrderRefundItemInfo {

	private String id;
	/** 已使用的数量 **/
	private String consume_count;
	/** 已退款的数量 **/
	private String refunded;
	/** 退款中的数量 **/
	private String refunding;
	/** 购买的总数量 **/
	private String number;
	/** 商品购买时的单价 **/
	private String unit_price;
	/** 订单编号 **/
	private String order_sn;
	/** 订单时间 **/
	private String create_time;
	/** 订单总金额 **/
	private String total_price;
	/** 订单总支付金额 **/
	private String pay_amount;
	/** 商品名称 **/
	private String name;

	public OrderRefundItemInfo() {
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getRefunding() {
		return refunding;
	}

	public void setRefunding(String refunding) {
		this.refunding = refunding;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
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

	public String getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(String pay_amount) {
		this.pay_amount = pay_amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OrderRefundItemInfo(String id, String consume_count, String refunded, String refunding, String number,
			String unit_price, String order_sn, String create_time, String total_price, String pay_amount,
			String name) {
		super();
		this.id = id;
		this.consume_count = consume_count;
		this.refunded = refunded;
		this.refunding = refunding;
		this.number = number;
		this.unit_price = unit_price;
		this.order_sn = order_sn;
		this.create_time = create_time;
		this.total_price = total_price;
		this.pay_amount = pay_amount;
		this.name = name;
	}

}
