package com.fanwe.model;

public class User_InfoActModel {

	private int id;
	private String user_name;
	private String user_pwd; // 加密过的密码
	private String email;    
	private String mobile;
	private int is_tmp;// 是否为临时会员 0:否 1:是
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_pwd() {
		return user_pwd;
	}
	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getIs_tmp() {
		return is_tmp;
	}
	public void setIs_tmp(int is_tmp) {
		this.is_tmp = is_tmp;
	}
	
	
}
