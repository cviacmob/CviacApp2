package com.cviac.com.cviac.app.restapis;

import java.util.Date;

/**
 * Created by User on 12/19/2016.
 */

public class AdditionalRegInfo {
    private String mobile;

    private String dob;

    private String otp;
    private String emp_code;


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }


    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

}
