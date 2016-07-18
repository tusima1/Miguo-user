package com.fanwe.model;

import java.util.List;

public class Uc_fx_my_fxActModel extends BaseActModel
{

	private MyDistributionUser_dataModel user_data;
	
	private List<DistributionGoodsModel> item;
	private String yuji;
	private int goods_count;
	
	private String level;
	private int level_id;
	
	private int collection_count;
	
	private int red_packet_count;
	
	private int user_num;
	
	
	
	public String getYuji() {
		return yuji;
	}

	public void setYuji(String yuji) {
		this.yuji = yuji;
	}

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

	public int getRed_packet_count() {
		return red_packet_count;
	}

	public void setRed_packet_count(int red_packet_count) {
		this.red_packet_count = red_packet_count;
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

	@Override
	public String toString() {
		return "Uc_fx_my_fxActModel [user_data=" + user_data + ", item=" + item
				+ ", goods_count=" + goods_count + ", collection_count="
				+ collection_count + ", red_packet_count=" + red_packet_count
				+ ", page=" + page + "]";
	}

	
	
}
