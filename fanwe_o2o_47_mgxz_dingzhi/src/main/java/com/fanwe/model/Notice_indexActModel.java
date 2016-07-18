package com.fanwe.model;

import java.util.List;

public class Notice_indexActModel extends BaseActModel
{
	private List<Notice_indexActListModel> list;

	private PageModel page;

	public List<Notice_indexActListModel> getList()
	{
		return list;
	}

	public void setList(List<Notice_indexActListModel> list)
	{
		this.list = list;
	}

	public PageModel getPage()
	{
		return page;
	}

	public void setPage(PageModel page)
	{
		this.page = page;
	}

}
