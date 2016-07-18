package com.fanwe.model;

import java.util.List;

public class Uc_withdrawActModel extends BaseActModel{

	private List<Uc_money_withdrawActModel>data;
	private PageModel page;
	public List<Uc_money_withdrawActModel> getData() {
		return data;
	}
	public void setData(List<Uc_money_withdrawActModel> data) {
		this.data = data;
	}
	public PageModel getPage() {
		return page;
	}
	public void setPage(PageModel page) {
		this.page = page;
	}
	
}
