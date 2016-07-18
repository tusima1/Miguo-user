package com.fanwe.model;

import java.util.List;

public class Uc_message_indexActModel extends BaseActModel 
{
//	'activity': { //最新一条活动消息(此字段仅在请求全部消息列表时返回)
//    'id': '2',
//    'type': '5',
//    'rel_id': '855', //关联数据ID
//    'act': 'deal', //动作
//    'content': '消息测试',
//    'status': '1', //1未读 2已读
//    'create_time': '2016-06-13 08:00',
//    'num': 10 //未读数量
//}
	private PageModel page;

	private List<Message> list;
	
	private Message_Activty activity;

	public PageModel getPage() {
		return page;
	}

	public void setPage(PageModel page) {
		this.page = page;
	}

	public List<Message> getList() {
		return list;
	}

	public void setList(List<Message> list) {
		this.list = list;
	}

	public Message_Activty getActivity() {
		return activity;
	}

	public void setActivity(Message_Activty activity) {
		this.activity = activity;
	}
	
	
	
}
