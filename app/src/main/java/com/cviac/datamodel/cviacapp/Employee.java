package com.cviac.datamodel.cviacapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(name = "employee")
public class Employee extends Model implements Serializable {

    @Column(name = "empid")
    private String empID;
    @Column(name = "name")
    private String name;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "emailid")
    private String emailID;
    @Column(name = "dob")
    private Date dob;
    @Column(name = "manager")
    private String managername;
    @Column(name = "gender")
    private String gender;
    @Column(name = "department")
    private String department;
    @Column(name = "designation")
    private String designation;


    public Employee() {
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



    public static List<Employee> getemployees() {
        return new Select()
                .from(Employee.class)
              //  .where("sender = ?", from)
                //.orderBy("Name ASC")
                .execute();

    }

    public static Employee getemployee(String id) {
        return new Select()
                .from(Employee.class)
                  .where("empid = ?", id)
                //.orderBy("Name ASC")
                .executeSingle();

    }



}
