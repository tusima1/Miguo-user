package com.fanwe.model;

public class Payment_ShareModel {
	
	private String title;
	private String summary;
	private String img;
	private String img_icon;
	private String logo;
	private String logo_icon;
	private String url;
	public Payment_ShareModel() {
		super();
	}
	public Payment_ShareModel(String title, String summary, String img, String img_icon, String logo,
			String logo_icon) {
		super();
		this.title = title;
		this.summary = summary;
		this.img = img;
		this.img_icon = img_icon;
		this.logo = logo;
		this.logo_icon = logo_icon;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getImg_icon() {
		return img_icon;
	}
	public void setImg_icon(String img_icon) {
		this.img_icon = img_icon;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getLogo_icon() {
		return logo_icon;
	}
	public void setLogo_icon(String logo_icon) {
		this.logo_icon = logo_icon;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	

}
