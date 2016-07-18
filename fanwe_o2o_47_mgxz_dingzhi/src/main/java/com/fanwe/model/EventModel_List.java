package com.fanwe.model;

import java.io.Serializable;

public class EventModel_List implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String special_name;
	private String special_price;
	private String special_desc;
	private String special_icon;
	private String origin_price;
	
	private String current_price;
	
	
	public String getCurrent_price() {
		return current_price;
	}
	public void setCurrent_price(String current_price) {
		this.current_price = current_price;
	}
	public String getSpecial_name() {
		return special_name;
	}
	public void setSpecial_name(String special_name) {
		this.special_name = special_name;
	}
	public String getSpecial_price() {
		return special_price;
	}
	public void setSpecial_price(String special_price) {
		this.special_price = special_price;
	}
	public String getSpecial_desc() {
		return special_desc;
	}
	public void setSpecial_desc(String special_desc) {
		this.special_desc = special_desc;
	}
	public String getSpecial_icon() {
		return special_icon;
	}
	public void setSpecial_icon(String special_icon) {
		this.special_icon = special_icon;
	}
	public String getOrigin_price() {
		return origin_price;
	}
	public void setOrigin_price(String origin_price) {
		this.origin_price = origin_price;
	}

}
