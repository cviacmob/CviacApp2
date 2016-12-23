package com.cviac.com.cviac.app.datamodels;

import java.util.List;

/**
 * Created by Shanmugam on 30-11-2016.
 */

public class EmployeeResponse {
    private List<Employee> Emp;

    public List<Employee> getEmployees() {
        return Emp;
    }

    public void setEmp(List<Employee> emp) {
        this.Emp = emp;
    }
}
