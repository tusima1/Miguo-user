package com.fanwe.model;

public class Member
{
	private String avatar;
	private String create_time;
	private String mobile;
	private String user_name;
	private int user_num;
	private int id;
	private int uid;
	private int rank;
	
	private String salary;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public int getUser_num() {
		return user_num;
	}
	public void setUser_num(int user_num) {
		this.user_num = user_num;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	@Override
	public String toString() {
		return "Member [avatar=" + avatar + ", create_time=" + create_time
				+ ", mobile=" + mobile + ", user_name=" + user_name
				+ ", user_num=" + user_num + ", id=" + id + ", rank=" + rank
				+ ", salary=" + salary + "]";
	}
	
	
}
