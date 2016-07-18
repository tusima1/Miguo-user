package com.fanwe.model;

import java.util.List;

public class Store_ActModel extends  BaseActModel{
   private String money;//余额
   private String page_title;
   private String store_name;
   private int discount_pay;
   private Float salary;
   
   private List<Payment_listModel> payment_list;
   
public Float getSalary() {
	return salary;
}
public void setSalary(Float salary) {
	this.salary = salary;
}
public String getMoney() {
	return money;
}
public void setMoney(String money) {
	this.money = money;
}
public String getPage_title() {
	return page_title;
}
public void setPage_title(String page_title) {
	this.page_title = page_title;
}
public String getStore_name() {
	return store_name;
}
public void setStore_name(String store_name) {
	this.store_name = store_name;
}
public int getDiscount_pay() {
	return discount_pay;
}
public void setDiscount_pay(int discount_pay) {
	this.discount_pay = discount_pay;
}
public List<Payment_listModel> getPayment_list() {
	return payment_list;
}
public void setPayment_list(List<Payment_listModel> payment_list) {
	this.payment_list = payment_list;
}
   
}
