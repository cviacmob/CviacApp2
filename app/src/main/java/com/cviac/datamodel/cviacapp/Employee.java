package com.cviac.datamodel.cviacapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(name = "employee")
public class Employee extends Model implements Serializable {

    @Column(name = "push_id")
    private String push_id;
    @Column(name = "emp_code",index = true)
    private String emp_code;
    @Column(name = "emp_name")
    private String emp_name;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "email")
    private String email;
    @Column(name = "dob")
    private Date dob;
    @Column(name = "manager")
    private String manager;
    @Column(name = "gender")
    private String gender;
    @Column(name = "department")
    private String department;
    @Column(name = "designation")
    private String designation;
    @Column(name = "status")
    private String status;
    @Column(name = "image_url")
    private String image_url;
    @Column(name = "doj")
    private Date doj;


    public Employee() {
        // TODO Auto-generated constructor stub
    } public String getPush_id() {
        return push_id;
    }

    public void setPush_id(String push_id) {
        this.push_id = push_id;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Date getDoj() {
        return doj;
    }

    public void setDoj(Date doj) {
        this.doj = doj;
    }

    public static List<Employee> getemployees() {
        return new Select()
                .from(Employee.class)
                //  .where("sender = ?", from)
                //.orderBy("Name ASC")
                .execute();

    }

    public static List<Employee> getemployees(String filterByName) {
        return new Select()
                .from(Employee.class)
                .where("Name = %?", filterByName)
                .orderBy("Name ASC")
                .execute();

    }

    public static Employee getemployee(String code) {
        return new Select()
                .from(Employee.class)
                .where("emp_code = ?", code)
                //.orderBy("Name ASC")
                .executeSingle();

    }

    public static Employee getemployeeByMobile(String mobile) {
        return new Select()
                .from(Employee.class)
                .where("mobile = ?", mobile)
                //.orderBy("Name ASC")
                .executeSingle();

    }

    public static void updateProfileImageUrl(String empcode, String url) {
        new Update(Employee.class)
                .set("image_url = ?", url)
                .where("emp_code = ?", empcode)
                .execute();
        return;
    }


}
