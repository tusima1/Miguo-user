package com.fanwe.model;

import java.util.List;

public class User_Order {
	
	private List<OrderOutItem> item;//每个大的订单数据
	private int status; // 1为正常
	private int page_now;// 当前页码
	private int page_size;// 页大小

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPage_now() {
		return page_now;
	}

	public void setPage_now(int page_now) {
		this.page_now = page_now;
	}

	public int getPage_size() {
		return page_size;
	}

	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}

	public List<OrderOutItem> getItem() {
		return item;
	}

	public void setItem(List<OrderOutItem> item) {
		this.item = item;
	}

}
