package com.cviac.com.cviac.app.restapis;

/**
 * Created by Administrator on 1/31/2017.
 */
public class UpdateStatusInfo {


    private String status;

    private String emp_code;
    public UpdateStatusInfo() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }
}
