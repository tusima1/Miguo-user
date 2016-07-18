package com.fanwe.model;

public class MessageCount {
	// "msg_normal": 10, //普通消息未读数量 type小于5的
	// "msg_activity": 5 //活动消息未读数量 type等于5的
	private int msg_normal;
	private int msg_activity;

	public int getMsg_normal() {
		return msg_normal;
	}

	public void setMsg_normal(int msg_normal) {
		this.msg_normal = msg_normal;
	}

	public int getMsg_activity() {
		return msg_activity;
	}

	public void setMsg_activity(int msg_activity) {
		this.msg_activity = msg_activity;
	}

}
