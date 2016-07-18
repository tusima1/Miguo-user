package com.fanwe.model;

/**
 * 
 * @author didikee
 * 退款页面
 *
 */
public class RefundItemInfo {
	/**一个退款订单具体内容**/
	private OrderRefundItemInfo order;
	private int status;
	private String info;
	
	public RefundItemInfo() {
		super();
	}

	public RefundItemInfo(OrderRefundItemInfo order, int status, String info) {
		super();
		this.order = order;
		this.status = status;
		this.info = info;
	}

	public OrderRefundItemInfo getOrder() {
		return order;
	}

	public void setOrder(OrderRefundItemInfo order) {
		this.order = order;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	

}
