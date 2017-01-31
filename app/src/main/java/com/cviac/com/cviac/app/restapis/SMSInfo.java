package com.cviac.com.cviac.app.restapis;

/**
 * Created by BALA on 10-01-2017.
 */

public class SMSInfo {
    private String mobile;
    private String msg;

    public SMSInfo(String mobile, String msg) {
        this.mobile = mobile;
        this.msg = msg;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
