package com.niu.crm.model;

import java.util.Date;

import com.niu.crm.model.type.ContactStatus;

public class PreContactRecord extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -916254498236805726L;

	
	private Long cstmId;
	private String contactType;
	private ContactStatus status;
	private Date contactDate;
	private Date nextDate; //下次回访时间
	private String contactText;
	private Long gwId;
	private String gwName;
	private Long creatorId;
	public Long getCstmId() {
		return cstmId;
	}
	public void setCstmId(Long cstmId) {
		this.cstmId = cstmId;
	}
	
	public String getContactType() {
		return contactType;
	}
	public void setContactType(String contactType) {
		this.contactType = contactType;
	}
	
	public String getStatusName() {
		if(status ==null)
			return null;
		else
			return status.getName();
	}
	public ContactStatus getStatus() {
		return status;
	}
	public void setStatus(ContactStatus status) {
		this.status = status;
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
	
	public String getContactText() {
		return contactText;
	}
	public void setContactText(String contactText) {
		this.contactText = contactText;
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
	
	
}
