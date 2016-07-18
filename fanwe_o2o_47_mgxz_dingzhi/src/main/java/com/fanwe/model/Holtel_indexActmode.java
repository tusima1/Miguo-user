package com.fanwe.model;

import java.util.List;

public class Holtel_indexActmode extends BaseActModel
{
 
	private List<Holtel_rangeAct> range;
	
	private List<Holtel_cityAct>quan_list;
	
	

	public List<Holtel_cityAct> getQuan_list() {
		return quan_list;
	}

	public void setQuan_list(List<Holtel_cityAct> quan_list) {
		this.quan_list = quan_list;
	}

	public List<Holtel_rangeAct> getRange() {
		return range;
	}

	public void setRange(List<Holtel_rangeAct> range) {
		this.range = range;
	}
	
	
}
