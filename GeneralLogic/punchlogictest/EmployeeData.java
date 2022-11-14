package com.ebcon.punchlogictest;

public class EmployeeData {
	private String employee;
	private TimePunch timePunch[];
	
	
	
	
	public EmployeeData() {
		super();
	}
	public EmployeeData(String employee, TimePunch[] timePunch) {
		super();
		this.employee = employee;
		this.timePunch = timePunch;
	}
	public String getEmployee() {
		return employee;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}
	public TimePunch[] getTimePunch() {
		return timePunch;
	}
	public void setTimePunch(TimePunch[] timePunch) {
		this.timePunch = timePunch;
	}
	
	
}
