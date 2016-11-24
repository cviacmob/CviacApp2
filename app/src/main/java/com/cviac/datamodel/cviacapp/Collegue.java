package com.cviac.datamodel.cviacapp;

import java.util.Date;

public class Collegue {
    private String empID;

    private String name;

    private String mobile;

    private String emailID;

    private Date dob;

    private String managername;
    private String gender;
    private String department;
    private String designation;


    public Collegue() {
        // TODO Auto-generated constructor stub
    }


    public String getEmailID() {
        return emailID;
    }

    public String getEmpID() {
        return empID;
    }


    public String getName() {
        return name;
    }

    public String getManagername() {
        return managername;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setManagername(String managername) {
        this.managername = managername;
    }


    public Date getDob() {
        return dob;
    }

    public String getMobile() {
        return mobile;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }


}
