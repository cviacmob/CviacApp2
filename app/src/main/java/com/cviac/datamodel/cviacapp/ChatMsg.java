package com.cviac.datamodel.cviacapp;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by Cviac on 28/11/2016.
 */

public class ChatMsg implements Serializable {

    private String msg;

    private boolean isIn;

    private Date ctime;

    private String from;

    private String name;



    public ChatMsg() {
        super();
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
