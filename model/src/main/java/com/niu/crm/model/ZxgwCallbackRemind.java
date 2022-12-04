package com.niu.crm.model;

import java.util.Date;

import com.niu.crm.model.type.CallbackRemindType;


public class ZxgwCallbackRemind extends BaseModel{
	private static final long serialVersionUID = -37499267559678533L;
	
	private Long stuId;
	
	//被提醒的顾问
	private Long zxgwId;
	
	//来源咨询顾问
	private Long sourceZxgwId;
	private CallbackRemindType remindType;

	//最迟应回访时间， 迟于此时间 算逾期
	private Date latestContactTime;
	
	//时间回访 信息
	private Long contactId; //t_lx_contact_record.id
	private Date contactTime; //回访时间
			
	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	
	public Long getZxgwId() {
		return zxgwId;
	}
	public void setZxgwId(Long zxgwId) {
		this.zxgwId = zxgwId;
	}

	public Long getSourceZxgwId() {
		return sourceZxgwId;
	}
	public void setSourceZxgwId(Long sourceZxgwId) {
		this.sourceZxgwId = sourceZxgwId;
	}
	
	public CallbackRemindType getRemindType() {
		return remindType;
	}
	public void setRemindType(CallbackRemindType remindType) {
		this.remindType = remindType;
	}
	
	public Date getLatestContactTime() {
		return latestContactTime;
	}
	public void setLatestContactTime(Date latestContactTime) {
		this.latestContactTime = latestContactTime;
	}
	
	public Long getContactId() {
		return contactId;
	}
	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}
	
	public Date getContactTime() {
		return contactTime;
	}
	public void setContactTime(Date contactTime) {
		this.contactTime = contactTime;
	}
}
