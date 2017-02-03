package com.cviac.com.cviac.app.restapis;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by BALA on 10-01-2017.
 */

public class GetStatus implements Serializable{

    private int code;
    private String desc;
    private String status;
    private Date last_activity;
    private String push_id;

    public GetStatus() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLast_activity() {
        return last_activity;
    }

    public void setLast_activity(Date last_activity) {
        this.last_activity = last_activity;
    }

    public String getPush_id() {
        return push_id;
    }

    public void setPush_id(String push_id) {
        this.push_id = push_id;
    }
}
