package com.fanwe.model;

public class User_infoModel extends BaseActModel
{
	private int id;
	private String user_id;
	private String user_name;
	private String user_pwd; // 加密过的密码
	private String email;    
	private String mobile;
	private int is_tmp;// 是否为临时会员 0:否 1:是
	public int getIs_tmp()
	{
		return is_tmp;
	}

	public void setIs_tmp(int is_tmp)
	{
		this.is_tmp = is_tmp;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getUser_pwd()
	{
		return user_pwd;
	}

	public void setUser_pwd(String user_pwd)
	{
		this.user_pwd = user_pwd;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getUser_name()
	{
		return user_name;
	}

	public void setUser_name(String user_name)
	{
		this.user_name = user_name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}
