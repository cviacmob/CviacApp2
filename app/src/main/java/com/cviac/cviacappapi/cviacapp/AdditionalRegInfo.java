package com.cviac.cviacappapi.cviacapp;

import java.util.Date;

/**
 * Created by User on 12/19/2016.
 */

public class AdditionalRegInfo {
    private String mobile;

    public String getEmpcode() {
        return empcode;
    }

    public void setEmpcode(String empcode) {
        this.empcode = empcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    private String empcode;
    private Date date;

    private String otp;
}
