package com.cviac.com.cviac.app.restapis;

/**
 * Created by User on 04-Jan-17.
 */

public class EmailResponse {
    private int code;
    private String desc;
    public EmailResponse(int code, String desc) {
        this.code = code;
        this.desc = desc;
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

}
