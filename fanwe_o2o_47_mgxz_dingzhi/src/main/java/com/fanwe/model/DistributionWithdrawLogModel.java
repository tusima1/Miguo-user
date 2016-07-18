package com.fanwe.model;

public class DistributionWithdrawLogModel
{
	private int id;
	private int dist_id;
	private Float money; // 提现金额
	private long create_time;
	
	private Float money_people;
	private String bank_info;
	private String bank_name;
	private String comment;
	private int status;
	private long update_time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDist_id() {
		return dist_id;
	}
	public void setDist_id(int dist_id) {
		this.dist_id = dist_id;
	}
	public Float getMoney() {
		return money;
	}
	public void setMoney(Float money) {
		this.money = money;
	}
	
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public Float getMoney_people() {
		return money_people;
	}
	public void setMoney_people(Float money_people) {
		this.money_people = money_people;
	}
	public String getBank_info() {
		return bank_info;
	}
	public void setBank_info(String bank_info) {
		this.bank_info = bank_info;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getComment()
	{
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(long update_time) {
		this.update_time = update_time;
	}
	
	
}
