package com.fanwe.model;

import java.util.List;

public class Payment_doneActModel extends BaseActModel
{
	private int pay_status;
	private int order_id;
	private String order_sn;
	private String pay_info;
	private Payment_codeModel payment_code;
	private String share_url;
	private String share_title;
	private String share_info;
	private String share_ico;
	private List<Payment_doneActCouponlistModel> couponlist;
	private Payment_ShareModel share;
	private float salary;

	
	public Payment_ShareModel getShare() {
		return share;
	}

	public void setShare(Payment_ShareModel share) {
		this.share = share;
	}

	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	public String getShare_url() 
	{
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
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

	public int getOrder_id()
	{
		return order_id;
	}

	public void setOrder_id(int order_id)
	{
		this.order_id = order_id;
	}

	public String getOrder_sn()
	{
		return order_sn;
	}

	public void setOrder_sn(String order_sn)
	{
		this.order_sn = order_sn;
	}

	public String getPay_info()
	{
		return pay_info;
	}

	public void setPay_info(String pay_info)
	{
		this.pay_info = pay_info;
	}

	
	public Payment_codeModel getPayment_code() {
		return payment_code;
	}

	public void setPayment_code(Payment_codeModel payment_code) {
		this.payment_code = payment_code;
	}

	public List<Payment_doneActCouponlistModel> getCouponlist()
	{
		return couponlist;
	}

	public void setCouponlist(List<Payment_doneActCouponlistModel> couponlist)
	{
		this.couponlist = couponlist;
	}

	public int getPay_status()
	{
		return pay_status;
	}

	public void setPay_status(int pay_status)
	{
		this.pay_status = pay_status;
	}
}
