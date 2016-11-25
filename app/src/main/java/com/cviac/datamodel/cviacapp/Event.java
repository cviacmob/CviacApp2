package com.cviac.datamodel.cviacapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

@Table(name = "events")
public class Event extends Model  {

    @Column(name = "title")
   private String title;
    @Column(name = "discription")
    private String discription;
    @Column(name = "date")
    private Date date;
    @Column(name = "event_id")
    private String event_id;

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public String getformatedDate() {
        return "2016/10/28";
    }
    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public static Event getevents() {
        return (Event) new Select()
                .from(Event.class)
                //.where("event_id = ?", id)
                //.orderBy("Name ASC")
            .execute();

    }

}
