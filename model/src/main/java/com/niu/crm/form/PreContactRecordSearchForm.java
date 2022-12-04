package com.niu.crm.form;

import java.text.ParseException;
import java.util.Date;

import com.niu.crm.model.type.ContactStatus;
import com.niu.crm.util.DateUtil;

public class PreContactRecordSearchForm {

	private Long cstmId;
	private Long gwId;
	
	private ContactStatus contactStatus;
	private Date nextDateFrom;
	private Date nextDateTo;
	private Boolean alarmed;
	
	private Date created_from;
	private Date created_to;
	
	
	public Long getCstmId() {
		return cstmId;
	}
	public void setCstmId(Long cstmId) {
		this.cstmId = cstmId;
	}
	
	public Long getGwId() {
		return gwId;
	}
	public void setGwId(Long gwId) {
		this.gwId = gwId;
	}
	
	public ContactStatus getContactStatus() {
		return contactStatus;
	}
	public void setContactStatus(ContactStatus contactStatus) {
		this.contactStatus = contactStatus;
	}

	public Date getNextDateFrom() {
		return nextDateFrom;
	}
	public void setNextDateFrom(Date nextDateFrom) {
		this.nextDateFrom = nextDateFrom;
	}
	
	public Date getNextDateTo() {
		return nextDateTo;
	}
	public void setNextDateTo(Date nextDateTo) {
		this.nextDateTo = nextDateTo;
	}
		
	public Date getCreated_from() {
		return created_from;
	}
	public void setCreated_from(String created_from) throws ParseException{
		this.created_from = DateUtil.parseDate(created_from);
	}
	
	public Date getCreated_to() {
		return created_to;
	}
	public void setCreated_to(String created_to) throws ParseException{
		this.created_to = DateUtil.parseDate(created_to);
	}
	
	public Boolean getAlarmed() {
		return alarmed;
	}
	public void setAlarmed(Boolean alarmed) {
		this.alarmed = alarmed;
	}

}
