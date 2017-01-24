package com.cviac.com.cviac.app.datamodels;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 1/24/2017.
 */
@Table(name = "Annoncements")
public class Annoncements extends Model {
    @Column(name = "annoncemsg")
    private String annoncemsg;
    @Column(name = "date")
    private Date date;

    public String getAnnoncemsg() {
        return annoncemsg;
    }

    public void setAnnoncemsg(String annoncemsg) {
        this.annoncemsg = annoncemsg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static List<Annoncements> getAnnoncements() {
        return new Select()
                .from(Annoncements.class)
                .orderBy("date DESC")
                .execute();
    }
}
