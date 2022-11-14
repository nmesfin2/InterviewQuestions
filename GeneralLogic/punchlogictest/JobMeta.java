package com.ebcon.punchlogictest;

public class JobMeta {
	private String job;
	private  double rate;
	private double benefitsRate;
	
	public JobMeta(String job, double rate, double benefitsRate) {
		this.job = job;
		this.rate = rate;
		this.benefitsRate = benefitsRate;
	}

	public JobMeta() {
		super();
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getBenefitsRate() {
		return benefitsRate;
	}

	public void setBenefitsRate(float benefitsRate) {
		this.benefitsRate = benefitsRate;
	}
	
	
	
}
