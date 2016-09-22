package com.fanwe.model;

public class User_RegisterAndLoginModel {

	private int verify_status;
	private User_infoModel user;
	private int status;
	private String info;
	
	
	public User_infoModel getUser() {
		return user;
	}
	public void setUser(User_infoModel user) {
		this.user = user;
	}
	public int getVerify_status() {
		return verify_status;
	}
	public void setVerify_status(int verify_status) {
		this.verify_status = verify_status;
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
