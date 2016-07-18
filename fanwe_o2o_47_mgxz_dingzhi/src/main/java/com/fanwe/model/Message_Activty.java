package com.fanwe.model;

public class Message_Activty {

	private String id;
	private String type;
	private String rel_id;
	private String act;
	private String content;
	private String create_time;
	private String title;
	private int num;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "Message_Activty [id=" + id + ", type=" + type + ", rel_id=" + rel_id + ", act=" + act + ", content="
				+ content + ", create_time=" + create_time + ", num=" + num + "]";
	}
	
	
}
