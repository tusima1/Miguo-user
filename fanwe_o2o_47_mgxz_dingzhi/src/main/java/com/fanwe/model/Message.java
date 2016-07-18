package com.fanwe.model;

public class Message {
	// 'id': '1',
	// 'type': '1', //消息类型 1红包 2下线 3佣金 4升级提醒 5活动类
	// 'rel_id': '0',//跳转团购门店的id
	// 'act': '',
	// 'content': '消息测试',
	// 'status': '1', //1未读 2已读
	// 'create_time': '2016-06-13 08:00'
	/*
	 * "id": "1", "type": "1", "content": "消息测试", "status": "2", "create_time":
	 * "1970-01-01 08:00"
	 */
	private String id;
	private String type;
	private String content;
	private String rel_id;
	private String act;
	private String status;
	private String title;
	private String create_time;
	private String follow_id;//极光推送的id
	
	
	public String getFollow_id() {
		return follow_id;
	}

	public void setFollow_id(String follow_id) {
		this.follow_id = follow_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRel_id() {
		return rel_id;
	}

	public void setRel_id(String rel_id) {
		this.rel_id = rel_id;
	}

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
