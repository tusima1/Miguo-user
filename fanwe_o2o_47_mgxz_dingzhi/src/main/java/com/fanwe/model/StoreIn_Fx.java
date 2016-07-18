package com.fanwe.model;

import java.util.List;

public class StoreIn_Fx extends BaseActModel
{
	private List<StoreIn_list> list;
	private PageModel page;
	private ShareModel share;
	
	
	public ShareModel getShare() {
		return share;
	}

	public void setShare(ShareModel share) {
		this.share = share;
	}

	public PageModel getPage()
	{
		return page;
	}

	public void setPage(PageModel page) 
	{
		this.page = page;
	}

	public List<StoreIn_list> getList() 
	{
		return list;
	}

	public void setList(List<StoreIn_list> list)
	{
		this.list = list;
	}

	
}
