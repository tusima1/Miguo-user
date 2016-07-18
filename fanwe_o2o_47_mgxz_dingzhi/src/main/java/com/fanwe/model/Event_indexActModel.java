package com.fanwe.model;

import java.util.List;

public class Event_indexActModel extends BaseActModel
{
	private Event_infoModel event_info;
	private List<CommentModel> dp_list;
	
	private EventModel_Time special;
	
	private List<Event_edtailModelList> list;
	
	private PageModel page;
	
	

	public EventModel_Time getSpecial() {
		return special;
	}

	public void setSpecial(EventModel_Time special) {
		this.special = special;
	}

	public List<Event_edtailModelList> getList() {
		return list;
	}

	public void setList(List<Event_edtailModelList> list) {
		this.list = list;
	}

	public PageModel getPage() {
		return page;
	}

	public void setPage(PageModel page) {
		this.page = page;
	}

	public Event_infoModel getEvent_info()
	{
		return event_info;
	}

	public void setEvent_info(Event_infoModel event_info)
	{
		this.event_info = event_info;
	}

	public List<CommentModel> getDp_list()
	{
		return dp_list;
	}

	public void setDp_list(List<CommentModel> dp_list)
	{
		this.dp_list = dp_list;
	}

}