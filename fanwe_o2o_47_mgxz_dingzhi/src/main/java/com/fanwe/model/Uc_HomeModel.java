package com.fanwe.model;

public class Uc_HomeModel extends BaseActModel
{
	private String user_name;
	private String user_avatar;
	private Home_DistModel dist;
	
	public String getUser_avatar() {
		return user_avatar;
	}
	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public Home_DistModel getDist() {
		return dist;
	}
	public void setDist(Home_DistModel dist) {
		this.dist = dist;
	}
	
}
