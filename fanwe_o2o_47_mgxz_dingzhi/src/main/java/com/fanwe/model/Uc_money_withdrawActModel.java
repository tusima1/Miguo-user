package com.fanwe.model;

public class Uc_money_withdrawActModel{
  /*  "id": "56",
    "user_id": "35741",
    "money": 100,
    "create_time": "2016-06-23 11:49:51",
    "is_paid": "0",
    "pay_time": "0",
    "bank_name": "招行 (...6632)",
    "bank_account": "************6632",
    "bank_user": "****t",
    "is_delete": "0",
    "bank_mobile": "13567109871",
    "is_bind": "1"*/

	private String create_time;
	private int id;
	private int user_id;
	private float money;
	private int is_paid;
	private long pay_time;
	private String bank_name;
	private String bank_user;
	private String bank_account;
	private String bank_mobile;
	private int is_bind;
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	public int getIs_paid() {
		return is_paid;
	}
	public void setIs_paid(int is_paid) {
		this.is_paid = is_paid;
	}
	public long getPay_time() {
		return pay_time;
	}
	public void setPay_time(long pay_time) {
		this.pay_time = pay_time;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getBank_user() {
		return bank_user;
	}
	public void setBank_user(String bank_user) {
		this.bank_user = bank_user;
	}
	public String getBank_account() {
		return bank_account;
	}
	public void setBank_account(String bank_account) {
		this.bank_account = bank_account;
	}
	public String getBank_mobile() {
		return bank_mobile;
	}
	public void setBank_mobile(String bank_mobile) {
		this.bank_mobile = bank_mobile;
	}
	public int getIs_bind() {
		return is_bind;
	}
	public void setIs_bind(int is_bind) {
		this.is_bind = is_bind;
	}
	
	
}
