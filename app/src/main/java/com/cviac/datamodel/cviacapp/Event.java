package com.cviac.datamodel.cviacapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Table(name = "events")
public class Event extends Model  {

    @Column(name = "event_title")
    private String event_title;
    @Column(name = "event_description")
    private String event_description;
    @Column(name = "event_date")
    private Date event_date;
    @Column(name = "event_id")
    private String event_id;


    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public Date getEvent_date() {
        return event_date;
    }

    public void setEvent_date(Date event_date) {
        this.event_date = event_date;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }



    public static List<Event> getevents() {
        return  new Select()
                .from(Event.class)
                .orderBy("event_date DESC")
            .execute();
    }








    public   String getformatteddate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event_date);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("dd/MM/yy");

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "TODAY";
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "YESTERDAY";
        } else {
            DateFormat dateform = new SimpleDateFormat("dd/MM/yy");
            return dateform.format(event_date);
        }

    }

}
