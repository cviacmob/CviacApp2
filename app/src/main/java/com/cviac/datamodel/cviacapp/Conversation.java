package com.cviac.datamodel.cviacapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

	public   String getformatteddate()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(datetime);
		Calendar today = Calendar.getInstance();
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DATE, -1);
		DateFormat timeFormatter = new SimpleDateFormat("HH:mm");

		if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
			return timeFormatter.format(datetime);
		} else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
			return "YESTERDAY";
		} else {
			DateFormat dateform = new SimpleDateFormat("dd/MM/yy");
			return dateform.format(datetime);
		}

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
