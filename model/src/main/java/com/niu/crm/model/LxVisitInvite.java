package com.niu.crm.model;

import java.util.Date;

public class LxVisitInvite extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 944910488325045560L;
	
	private Long stuId;
	private Long gwId;
	
	private Date appointmentTime;
	private String appointmentMemo;
	private Long creatorId;
	
	private Date visitTime;
	private String visitMemo;
	private Long visitCreator;
	private Date visitCreated;
	
	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	
	public Long getGwId() {
		return gwId;
	}
	public void setGwId(Long gwId) {
		this.gwId = gwId;
	}
	
	public Date getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(Date appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	
	public String getAppointmentMemo() {
		return appointmentMemo;
	}
	public void setAppointmentMemo(String appointmentMemo) {
		this.appointmentMemo = appointmentMemo;
	}
	
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	
	public Date getVisitTime() {
		return visitTime;
	}
	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}
	
	public String getVisitMemo() {
		return visitMemo;
	}
	public void setVisitMemo(String visitMemo) {
		this.visitMemo = visitMemo;
	}
	
	public Long getVisitCreator() {
		return visitCreator;
	}
	public void setVisitCreator(Long visitCreator) {
		this.visitCreator = visitCreator;
	}
	
	public Date getVisitCreated() {
		return visitCreated;
	}
	public void setVisitCreated(Date visitCreated) {
		this.visitCreated = visitCreated;
	}
}
