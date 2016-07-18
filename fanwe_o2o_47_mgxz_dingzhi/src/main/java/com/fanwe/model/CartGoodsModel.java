package com.fanwe.model;

import java.io.Serializable;

import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.utils.SDFormatUtil;

public class CartGoodsModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private int return_score;
	private int return_total_score;
	/**
	 * 商品单价(折后价格).
	 */
	private String unit_price;
	/**
	 * 总价。
	 */
	private String total_price;
	/**
	 * 原价。
	 */
	private String origin_price;
	private int number;
	private int deal_id;
	private String attr;
	private String name;
	private String sub_name;
	/**
	 * max为老版本兼容字段
	 * user_max_bought 为现在版本使用的购物数量限制 -1:无限制;其他数值表示限购数量;
	 */
	private int max;
	private int user_max_bought;
	
	private String icon;
	/**
	 * 是否首单 传 0，1
	 */
	private int is_first;
	/**
	 * 首单价格
	 */
	private float is_first_price;
	private int check_first;
	/**
	 * 红包数量
	 */
	private int red_packet;
	/**
	 * 红包价格。
	 */
	private float red_packet_money;
	/**
	 * 是否被选中。
	 */
	private boolean isChecked = false;
	/**
	 * 是否编辑状态下被选中。
	 */
	private boolean isEdit=false;
	/**
	 * 小计总额。
	 */
	private float sumPrice =0.00f;
	/**
	 * 1 进行中，2 过期，3 未开始
	 */
	private int time_status;
	/**
	 * 0未成团 ，1 成团， 2 卖光
	 */
	private int buy_status;
	
	

	
	public int getUser_max_bought() {
		return user_max_bought;
	}

	public void setUser_max_bought(int user_max_bought) {
		this.user_max_bought = user_max_bought;
	}

	public String getOrigin_price() {
		return origin_price;
	}

	public void setOrigin_price(String origin_price) {
		this.origin_price = origin_price;
	}

	public int getRed_packet() {
		return red_packet;
	}

	public void setRed_packet(int red_packet) {
		this.red_packet = red_packet;
	}

	public float getRed_packet_money() {
		return red_packet_money;
	}

	public void setRed_packet_money(float red_packet_money) {
		this.red_packet_money = red_packet_money;
	}

	public int getIs_first() {
		return is_first;
	}

	public void setIs_first(int is_first) {
		this.is_first = is_first;
	}

	public float getIs_first_price() {
		return is_first_price;
	}

	public void setIs_first_price(float is_first_price) {
		this.is_first_price = is_first_price;
	}

	public int getCheck_first() {
		return check_first;
	}

	public void setCheck_first(int check_first) {
		this.check_first = check_first;
	}

	// add
	private String unit_priceFormat;
	private double unit_priceDouble;

	public String getUnit_priceFormat() {
		return unit_priceFormat;
	}

	public String getTotal_priceFormat() {
		return SDFormatUtil.formatMoneyChina(this.unit_priceDouble * number);
	}

	public String getReturn_scoreFormat() {
		return return_score + "积分";
	}

	public String getReturn_total_scoreFormat() {
		return return_score * number + "积分";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReturn_score() {
		return return_score;
	}

	public void setReturn_score(int return_score) {
		this.return_score = return_score;
	}

	public int getReturn_total_score() {
		return return_total_score;
	}

	public void setReturn_total_score(int return_total_score) {
		this.return_total_score = return_total_score;
	}

	public String getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
		this.unit_priceFormat = SDFormatUtil.formatMoneyChina(unit_price);
		this.unit_priceDouble = SDTypeParseUtil.getDouble(unit_price);
	}

	public String getTotal_price() {
		return total_price;
	}

	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getDeal_id() {
		return deal_id;
	}

	public void setDeal_id(int deal_id) {
		this.deal_id = deal_id;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSub_name() {
		return sub_name;
	}

	public void setSub_name(String sub_name) {
		this.sub_name = sub_name;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public float getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(float sumPrice) {
		this.sumPrice = sumPrice;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	public int getTime_status() {
		return time_status;
	}

	public void setTime_status(int time_status) {
		this.time_status = time_status;
	}

	public int getBuy_status() {
		return buy_status;
	}

	public void setBuy_status(int buy_status) {
		this.buy_status = buy_status;
	}

	

	
	

}