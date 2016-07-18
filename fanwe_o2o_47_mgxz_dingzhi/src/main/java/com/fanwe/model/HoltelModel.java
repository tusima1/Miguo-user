package com.fanwe.model;

import java.util.List;

public class HoltelModel extends BaseActModel{

	
	private int page_now;
	private  int page_size;
	
	private List<HoltelModel_list>list;

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

	public List<HoltelModel_list> getList() {
		return list;
	}

	public void setList(List<HoltelModel_list> list) {
		this.list = list;
	}
	
	
}
