package com.fanwe.model;

import java.util.List;

public class User_center_indexActModel extends BaseActModel
{

	private int uid;
	private String user_name;
	private String user_money_format;
	private String user_avatar;
	private String yuji;
	private String user_money;
	private String user_score;
	private String coupon_count;
	private String youhui_count;
	private String wait_dp_count;
	private String not_pay_order_count;
	private String evaluate_count;
	
	private String indent_count;
	private String hongbao_count;
	
	private String has_dp_count;
	private String collect_deal_count;
	private String order_refund_count;
	private String red_packet_count;
	private String wait_use_count;
	private String up_name;
	private int up_id;
	
private MyDistributionUser_dataModel user_data;
	
	private List<DistributionGoodsModel> item;
	private int goods_count;
	
	private String level;
	private int level_id;
	
	private int collection_count;
	
	private int user_num;
	
	
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getLevel_id() {
		return level_id;
	}

	public void setLevel_id(int level_id) {
		this.level_id = level_id;
	}

	public int getUser_num() {
		return user_num;
	}

	public void setUser_num(int user_num) {
		this.user_num = user_num;
	}

	public int getGoods_count() {
		return goods_count;
	}

	public void setGoods_count(int goods_count) {
		this.goods_count = goods_count;
	}

	public int getCollection_count() {
		return collection_count;
	}

	public void setCollection_count(int collection_count) {
		this.collection_count = collection_count;
	}

	public MyDistributionUser_dataModel getUser_data()
	{
		return user_data;
	}

	public void setUser_data(MyDistributionUser_dataModel user_data)
	{
		this.user_data = user_data;
	}

	public List<DistributionGoodsModel> getItem()
	{
		return item;
	}

	public void setItem(List<DistributionGoodsModel> item)
	{
		this.item = item;
	}

	private PageModel page;

	public PageModel getPage()
	{
		return page;
	}

	public void setPage(PageModel page)
	{
		this.page = page;
	}
	
	
	public String getUp_name() {
		return up_name;
	}

	public void setUp_name(String up_name) {
		this.up_name = up_name;
	}

	public int getUp_id() {
		return up_id;
	}

	public void setUp_id(int up_id) {
		this.up_id = up_id;
	}

	public String getYuji() {
		return yuji;
	}

	public void setYuji(String yuji) {
		this.yuji = yuji;
	}

	public String getWait_use_count() {
		return wait_use_count;
	}

	public void setWait_use_count(String wait_use_count) {
		this.wait_use_count = wait_use_count;
	}

	public String getHas_dp_count() {
		return has_dp_count;
	}

	public void setHas_dp_count(String has_dp_count)
	{
		this.has_dp_count = has_dp_count;
	}

	public String getCollect_deal_count() {
		return collect_deal_count;
	}

	public void setCollect_deal_count(String collect_deal_count) {
		this.collect_deal_count = collect_deal_count;
	}

	public String getOrder_refund_count() {
		return order_refund_count;
	}

	public void setOrder_refund_count(String order_refund_count) {
		this.order_refund_count = order_refund_count;
	}

	public String getRed_packet_count() {
		return red_packet_count;
	}

	public void setRed_packet_count(String red_packet_count) {
		this.red_packet_count = red_packet_count;
	}


	public String getWait_dp_count()
	{
		return wait_dp_count;
	}

	public void setWait_dp_count(String wait_dp_count)
	{
		this.wait_dp_count = wait_dp_count;
	}

	public String getUser_avatar()
	{
		return user_avatar;
	}

	public void setUser_avatar(String user_avatar)
	{
		this.user_avatar = user_avatar;
	}

	public int getUid()
	{
		return uid;
	}

	public void setUid(int uid)
	{
		this.uid = uid;
	}

	public String getUser_name()
	{
		return user_name;
	}

	public void setUser_name(String user_name)
	{
		this.user_name = user_name;
	}

	public String getUser_money()
	{
		return user_money;
	}

	public void setUser_money(String user_money)
	{
		this.user_money = user_money;
	}

	public String getUser_money_format()
	{
		return user_money_format;
	}

	public void setUser_money_format(String user_money_format)
	{
		this.user_money_format = user_money_format;
	}

	public String getUser_score()
	{
		return user_score;
	}

	public void setUser_score(String user_score)
	{
		this.user_score = user_score;
	}

	public String getCoupon_count()
	{
		return coupon_count;
	}
	public void setCoupan_count(String coupan_count)
	{
		this.coupon_count=coupan_count;
	}
	public String getEvaluate_count()
	{
		return evaluate_count;
	}
	public void setEvaluate_count(String evaluate_count)
	{
		this.evaluate_count=evaluate_count;
	}
	public String getIndent_count()
	{
		return indent_count;
	}
	public void setIndent_count(String indent_count)
	{
		this.indent_count=indent_count;
	}

	public void setCoupon_count(String coupon_count)
	{
		this.coupon_count = coupon_count;
	}
	public  String getHongbao_count()
	{
		return hongbao_count;
	}
	public void setHongbao_count(String hongbao_count)
	{
		this.hongbao_count=hongbao_count;
	}
	public String getYouhui_count()
	{
		return youhui_count;
	}

	public void setYouhui_count(String youhui_count)
	{
		this.youhui_count = youhui_count;
	}

	public String getNot_pay_order_count()
	{
		return not_pay_order_count;
	}

	public void setNot_pay_order_count(String not_pay_order_count)
	{
		this.not_pay_order_count = not_pay_order_count;
	}

}
