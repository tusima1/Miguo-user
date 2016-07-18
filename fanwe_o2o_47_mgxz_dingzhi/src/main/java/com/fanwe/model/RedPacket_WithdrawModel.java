package com.fanwe.model;

public class RedPacket_WithdrawModel {
//	"share_url": "http://w2.mgxz.com/wap/index.php?ctl=red_packet&act=share&code=MzQxNTQ1MTI3NNczNjWxNAQA",
//    "share_title": "米果小站送您新年大礼 点击领取",
//    "share_info": "米果小站送您新年大礼 现金红包免费领",
//    "share_ico": "http://w2.mgxz.com/public/images/logo.png",
//    "page_title": "米果小站 - 营销红包",
	
	private String share_url;
	private String share_title;
	private String share_info;
	private String share_ico;
	private String page_title;
	private int rp_id;
	private int status;
	public RedPacket_WithdrawModel(String share_url, String share_title,
			String share_info, String share_ico, String page_title, int rp_id,
			int status) {
		super();
		this.share_url = share_url;
		this.share_title = share_title;
		this.share_info = share_info;
		this.share_ico = share_ico;
		this.page_title = page_title;
		this.rp_id = rp_id;
		this.status = status;
	}
	
	public RedPacket_WithdrawModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getShare_url() {
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
	public String getPage_title() {
		return page_title;
	}
	public void setPage_title(String page_title) {
		this.page_title = page_title;
	}
	public int getRp_id() {
		return rp_id;
	}
	public void setRp_id(int rp_id) {
		this.rp_id = rp_id;
	}
	
	
}
