package com.fanwe.model;

import java.util.List;

public class Uc_ecv_indexActModel extends BaseActModel
{

	private List<RedEnvelopeModel> list;
	private String user_avatar;
	private int ecv_count;
	private double ecv_total;

	private PageModel page;
	private int invalid;
	

	public int getInvalid() {
		return invalid;
	}

	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}

	public List<RedEnvelopeModel> getList()
	{
		return list;
	}

	public void setList(List<RedEnvelopeModel> list)
	{
		this.list = list;
	}

	public String getUser_avatar()
	{
		return user_avatar;
	}

	public void setUser_avatar(String user_avatar)
	{
		this.user_avatar = user_avatar;
	}

	public int getEcv_count()
	{
		return ecv_count;
	}

	public void setEcv_count(int ecv_count)
	{
		this.ecv_count = ecv_count;
	}

	public double getEcv_total()
	{
		return ecv_total;
	}

	public void setEcv_total(double ecv_total)
	{
		this.ecv_total = ecv_total;
	}

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
		return "Uc_ecv_indexActModel [data=" + list + ", user_avatar="
				+ user_avatar + ", ecv_count=" + ecv_count + ", ecv_total="
				+ ecv_total + ", page=" + page + "]";
	}
	
}
