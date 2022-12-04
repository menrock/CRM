package com.niu.crm.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.niu.crm.model.type.CallbackType;
import com.niu.crm.model.type.ContactStatus;


public class LxContactRecord extends BaseModel{
	private static final long serialVersionUID = -5254504846547494911L;
	
	private Long stuId;
	private CallbackType callbackType;
	private String contactType;
	private ContactStatus contactStatus;
	private Date contactDate;
	private Date nextDate; //下次回访时间
	private String contactText;
	private String caseText; //
	private Long gwId;
	private String gwName;
	private Long creatorId;
	private Date updatedAt;
	private Boolean oldFlag =Boolean.FALSE;  //是否是历史数据

	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}

	public CallbackType getCallbackType() {
		return callbackType;
	}
	public void setCallbackType(CallbackType callbackType) {
		this.callbackType = callbackType;
	}
	
	public String getContactType() {
		return contactType;
	}
	public void setContactType(String contactType) {
		this.contactType = contactType;
	}
	
	public ContactStatus getContactStatus() {
		return contactStatus;
	}
	public void setContactStatus(ContactStatus contactStatus) {
		this.contactStatus = contactStatus;
	}
	
	public String getContactText() {
		return contactText;
	}
	public void setContactText(String contactText) {
		this.contactText = contactText;
	}

	public String getCaseText() {
		return caseText;
	}
	public void setCaseText(String caseText) {
		this.caseText = caseText;
	}
	
	public Date getContactDate() {
		return contactDate;
	}
	public void setContactDate(Date contactDate) {
		this.contactDate = contactDate;
	}

	public Date getNextDate() {
		return nextDate;
	}
	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}
	
	public Long getGwId() {
		return gwId;
	}
	public void setGwId(Long gwId) {
		this.gwId = gwId;
	}
	
	public String getGwName() {
		return gwName;
	}
	public void setGwName(String gwName) {
		this.gwName = gwName;
	}

	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Boolean getOldFlag() {
		return oldFlag;
	}
	public void setOldFlag(Boolean oldFlag) {
		this.oldFlag = oldFlag;
	}
}