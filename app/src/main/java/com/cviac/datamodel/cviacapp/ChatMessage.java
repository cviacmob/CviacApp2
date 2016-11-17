package com.cviac.datamodel.cviacapp;

import java.util.Date;

public class ChatMessage {
	private String msg;
	private boolean isIn;
	private Date ctime;
	private String from;
	
	

	public ChatMessage() {

	}

	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public void setIn(boolean isIn) {
		this.isIn = isIn;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	
	public boolean isIn() {
		return isIn;
	}
}
