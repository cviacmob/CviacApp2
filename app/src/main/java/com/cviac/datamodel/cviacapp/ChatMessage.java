package com.cviac.datamodel.cviacapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;


@Table(name = "ChatMessages")
public class ChatMessage extends Model {

	@Column(name = "msg")
	private String msg;

	@Column(name = "isIn")
	private boolean isIn;

	@Column(name = "ctime")
	private String ctime;

	@Column(name = "sender")
	private String from;

	public ChatMessage() {
		super();
	}

	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
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

	public static List<ChatMessage> getAll(String from) {
		return new Select()
				.from(ChatMessage.class)
				.where("sender = ?", from)
				//.orderBy("Name ASC")
				.execute();
	}


}
