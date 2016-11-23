package com.cviac.datamodel.cviacapp;

import java.io.Serializable;
import java.util.Date;

public class Employee {

	private String empID;

	private String name;

	private String mobile;

	private String emailID;

	private Date dob;
	private String gender;

	private String managername;
			//name;
	private String department;

	private String designation;
	private int Status;

	public Employee() {
		// TODO Auto-generated constructor stub
	}

	public String getEmailID() {
		return emailID;
	}

	public String getEmpID() {
		return empID;
	}

	public String getManagername() {
		return managername;
	}

	public String getName() {
		return name;
	}

	public Date getDob() {
		return dob;
	}

	public String getMobile() {
		return mobile;
	}

	public String getGender() {
		return gender;

	}

	public String getDepartment() {
		return department;
	}

	public String getDesignation() {
		return designation;
	}

	public int getStatus() {
		return Status;

	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setdepartment(String department) {
		this.department = department;
	}

	public void setDegination(String designation) {
		this.designation = designation;
	}

	public void setstatus(int status) {
		this.Status = status;
	}

	public String getformatedDate(){
		return "1998/10/28";
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public void setEmpID(String empID) {
		this.empID = empID;
	}


	public void setManagername(String managername) {
		this.managername = managername;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setName(String name) {
		this.name = name;
	}

}

