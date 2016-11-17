package com.cviac.datamodel.cviacapp;

import java.util.Date;

public class Collegue {
	private String empID;
	
	private String name;
	
	private String mobile;
	
	private String emailID;
	
	private Date dob;
	
	private String managerID;
	
	
	public Collegue() {
		// TODO Auto-generated constructor stub
	}
	
	
	public String getEmailID() {
		return emailID;
	}
	
	public String getEmpID() {
		return empID;
	}
	
	public String getManagerID() {
		return managerID;
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
	
	public void setDob(Date dob) {
		this.dob = dob;
	}
	
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	
	public void setEmpID(String empID) {
		this.empID = empID;
	}
	
	public void setManagerID(String managerID) {
		this.managerID = managerID;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	

}
