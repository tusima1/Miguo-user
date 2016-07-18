package com.fanwe.model;


public class UserInfoModel extends BaseActModel
{
	private int verify_status;
	private User_infoModel user;
	public int getVerify_status() {
		return verify_status;
	}

	public void setVerify_status(int verify_status) {
		this.verify_status = verify_status;
	}

	public User_infoModel getUser() {
		return user;
	}

	public void setUser(User_infoModel user) {
		this.user = user;
	}
}
