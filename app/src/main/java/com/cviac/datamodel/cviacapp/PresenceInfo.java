package com.cviac.datamodel.cviacapp;

import java.util.Date;

/**
 * Created by Cviac on 15/12/2016.
 */

public class PresenceInfo {
    String status;
    Date lastseen;

    public PresenceInfo() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastseen() {
        return lastseen;
    }

    public void setLastseen(Date lastseen) {
        this.lastseen = lastseen;
    }
}
