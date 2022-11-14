package com.ebcon.punchlogictest;

import java.util.Date;

public class TimePunch {
	private String job;
	private Date start;
	private Date end;
	
	
	public TimePunch() {
		
	}
	public TimePunch(String job, Date start, Date end) {
		this.job = job;
		this.start = start;
		this.end = end;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	
	
	
}
