package com.fanwe.model;

public class MalipayModel
{

	private String subject;
	private String body;
	private String total_fee;
	private String total_fee_format;
	private String out_trade_no;
	private String notify_url;
	private String partner;
	private String seller_id;
	private String order_spec;
	private String sign;
	private String sign_type;
	private String _input_charset;
	private String service;
	private String payment_type;

	/**
	 * 签名和签名内容。未加入的串
	 */
	private String textHtml;

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public String getTotal_fee()
	{
		return total_fee;
	}

	public void setTotal_fee(String total_fee)
	{
		this.total_fee = total_fee;
	}

	public String getTotal_fee_format()
	{
		return total_fee_format;
	}

	public void setTotal_fee_format(String total_fee_format)
	{
		this.total_fee_format = total_fee_format;
	}

	public String getOut_trade_no()
	{
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no)
	{
		this.out_trade_no = out_trade_no;
	}

	public String getNotify_url()
	{
		return notify_url;
	}

	public void setNotify_url(String notify_url)
	{
		this.notify_url = notify_url;
	}

	public String getPartner()
	{
		return partner;
	}

	public void setPartner(String partner)
	{
		this.partner = partner;
	}

	public String getSeller_id()
	{
		return seller_id;
	}

	public void setSeller_id(String seller_id)
	{
		this.seller_id = seller_id;
	}

	public String getOrder_spec()
	{
		return order_spec;
	}

	public void setOrder_spec(String order_spec)
	{
		this.order_spec = order_spec;
	}

	public String getSign()
	{
		return sign;
	}

	public void setSign(String sign)
	{
		this.sign = sign;
	}

	public String getSign_type()
	{
		return sign_type;
	}

	public void setSign_type(String sign_type)
	{
		this.sign_type = sign_type;
	}

	public String get_input_charset() {
		return _input_charset;
	}

	public void set_input_charset(String _input_charset) {
		this._input_charset = _input_charset;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getTextHtml() {
		return textHtml;
	}

	public void setTextHtml(String textHtml) {
		this.textHtml = textHtml;
	}
}
