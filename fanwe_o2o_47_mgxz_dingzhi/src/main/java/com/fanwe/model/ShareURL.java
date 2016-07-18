package com.fanwe.model;

public class ShareURL {
	
	private String share_url;
	private int user_login_status;
	private int status;
	private String share_title;
	private String share_info;
	private String share_ico;
	private String share_rule;
	
	
	
	
	public String getShare_rule() {
		return share_rule;
	}
	public void setShare_rule(String share_rule) {
		this.share_rule = share_rule;
	}
	public String getShare_title() {
		return share_title;
	}
	public void setShare_title(String share_title) {
		this.share_title = share_title;
	}
	public String getShare_info() {
		return share_info;
	}
	public void setShare_info(String share_info) {
		this.share_info = share_info;
	}
	public String getShare_ico() {
		return share_ico;
	}
	public void setShare_ico(String share_ico) {
		this.share_ico = share_ico;
	}
	public String getShare_url() {
		return share_url;
	}
	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}
	public int getUser_login_status() {
		return user_login_status;
	}
	public void setUser_login_status(int user_login_status) {
		this.user_login_status = user_login_status;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "ShareURL [share_url=" + share_url + ", user_login_status="
				+ user_login_status + ", status=" + status + ", share_title="
				+ share_title + ", share_info=" + share_info + ", share_ico="
				+ share_ico + "]";
	}
	
}
