package com.niu.crm.model;

import java.util.Date;

public class CallbackCheckDetail extends BaseModel{
	private static final long serialVersionUID = 3710260466408575577L;
	
	private Long stuId;
	private Long cstmId;
	private String cstmName;
	private Long stuFromId;
	private Date latestContactTime; 
	private Date contactTime; //回访时间
	

	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	
	public Long getCstmId() {
		return cstmId;
	}
	public void setCstmId(Long cstmId) {
		this.cstmId = cstmId;
	}
	public String getCstmName() {
		return cstmName;
	}
	public void setCstmName(String cstmName) {
		this.cstmName = cstmName;
	}
	public Long getStuFromId() {
		return stuFromId;
	}
	public void setStuFromId(Long stuFromId) {
		this.stuFromId = stuFromId;
	}
	public Date getLatestContactTime() {
		return latestContactTime;
	}
	public void setLatestContactTime(Date latestContactTime) {
		this.latestContactTime = latestContactTime;
	}
	public Date getContactTime() {
		return contactTime;
	}
	public void setContactTime(Date contactTime) {
		this.contactTime = contactTime;
	}

}
