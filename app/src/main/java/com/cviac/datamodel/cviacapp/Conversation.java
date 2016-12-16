package com.cviac.datamodel.cviacapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(name = "conversation")
public class Conversation  extends Model implements Serializable{

	@Column(name = "imageurl")
	private String imageurl;
	@Column(name = "name")
	private String name;
	@Column(name = "lastmsg")
	private String lastmsg;
	@Column(name = "timestmp")
	private Date datetime;
	@Column(name = "empid",index = true)
	private String empid;

	public Conversation() {
	}

	public String getImageurl() {
		return imageurl;
	}

	public String getName() {
		return name;
	}

	public String getLastmsg() {
		return lastmsg;
	}

	public Date getDatetime() {
		return datetime;
	}

	public String getEmpid() {
		return empid;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public void setLastmsg(String lastmsg) {
		this.lastmsg = lastmsg;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	public String getformatedDate() {
		return "2016/10/28";
	}

	public static List<Conversation> getConversations() {
		return new Select()
				.from(Conversation.class)
				.orderBy("timestmp DESC")
				.execute();
	}

	public static Conversation getConversation(String empCode) {
		return new Select()
				.from(Conversation.class)
				.where("empid = ?", empCode)
				.executeSingle();
	}

}
