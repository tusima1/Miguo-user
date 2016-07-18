package com.fanwe.model;

public class JpushActModel {

	private int object_id;
	private int messsage_type;
	private String message_act;
	public int getObject_id() {
		return object_id;
	}
	public void setObject_id(int object_id) {
		if (object_id==0) {
			object_id =-1;
		}else{
			this.object_id = object_id;
		}
	}
	public int getMesssage_type() {
		return messsage_type;
	}
	public void setMesssage_type(int messsage_type) {
		if (messsage_type == 0) {
			messsage_type =-1;
		}else{
			this.messsage_type = messsage_type;
		}
	}
	public String getMessage_act() {
		return message_act;
	}
	public void setMessage_act(String message_act) {
		if ("".equals(message_act)) {
			message_act ="";
		}else{
			this.message_act = message_act;
		}
		
	}
	
	
}
