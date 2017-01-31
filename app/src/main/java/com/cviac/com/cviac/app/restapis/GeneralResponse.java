package com.cviac.com.cviac.app.restapis;

/**
 * Created by BALA on 10-01-2017.
 */

public class GeneralResponse {

    private int code;
    private String desc;

    public GeneralResponse(int code, String desc) {
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
