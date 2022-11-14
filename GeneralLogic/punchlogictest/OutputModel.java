package com.ebcon.punchlogictest;

public class OutputModel {
	private String employeee;
	private double regular;
    private double overtime;
    private double doubletime;
    private double wageTotal;
    private double benefitTotal;
    
    
    
	public OutputModel() {
	
	}
	public OutputModel(String employeee, float regular, float overtime, float doubletime, double wageTotal,
			double benefitTotal) {
		super();
		this.employeee = employeee;
		this.regular = regular;
		this.overtime = overtime;
		this.doubletime = doubletime;
		this.wageTotal = wageTotal;
		this.benefitTotal = benefitTotal;
	}
	public String getEmployeee() {
		return employeee;
	}
	public void setEmployeee(String employeee) {
		this.employeee = employeee;
	}
	public double getRegular() {
		return regular;
	}
	public void setRegular(double currHour) {
		this.regular = currHour;
	}
	public double getOvertime() {
		return overtime;
	}
	public void setOvertime(double d) {
		this.overtime = d;
	}
	public double getDoubletime() {
		return doubletime;
	}
	public void setDoubletime(double doubletime) {
		this.doubletime = doubletime;
	}
	public double getWageTotal() {
		return wageTotal;
	}
	public void setWageTotal(double wageTotal) {
		this.wageTotal = wageTotal;
	}
	public double getBenefitTotal() {
		return benefitTotal;
	}
	public void setBenefitTotal(double benefitTotal) {
		this.benefitTotal = benefitTotal;
	}
	@Override
	public String toString() {
		return "OutputModel [employeee=" + employeee + ", regular=" + regular + ", overtime=" + overtime
				+ ", doubletime=" + doubletime + ", wageTotal=" + wageTotal + ", benefitTotal=" + benefitTotal + "]";
	}
    
	
    
	
}
