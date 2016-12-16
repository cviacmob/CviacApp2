package com.cviac.datamodel.cviacapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    static  Date date = new Date();
    static String st=dateFormat.format(date);


    public static Event getevents() {
        return (Event) new Select()
                .from(Event.class)
                .where("event_date = ?",st)
                //.orderBy("Name ASC")
            .execute();

    }

}
