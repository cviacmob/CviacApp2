package com.cviac.datamodel.cviacapp;

import java.util.Date;

public class Conversation {
	String imageurl;
	String name;
	String lastmsg;
	Date datetime;
	String empid;

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

}
