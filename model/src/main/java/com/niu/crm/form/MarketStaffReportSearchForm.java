package com.niu.crm.form;

import java.util.Date;

public class MarketStaffReportSearchForm {
	private Long companyId;
	private Long ownerId;
	private Date createdFrom;
	private Date createdTo;
	
	private Long stuFromId;
	private String stuFromCode;
	
	private Long[] stuLevels;

	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Date getCreatedFrom() {
		return createdFrom;
	}
	public void setCreatedFrom(Date createdFrom) {
		this.createdFrom = createdFrom;
	}

	public Date getCreatedTo() {
		return createdTo;
	}
	public void setCreatedTo(Date createdTo) {
		this.createdTo = createdTo;
	}

	public Long[] getStuLevels() {
		return stuLevels;
	}
	public void setStuLevels(Long[] stuLevels) {
		this.stuLevels = stuLevels;
	}
	
	public Long getStuFromId() {
		return stuFromId;
	}
	public void setStuFromId(Long stuFromId) {
		this.stuFromId = stuFromId;
	}
	
	public String getStuFromCode() {
		return stuFromCode;
	}
	public void setStuFromCode(String stuFromCode) {
		this.stuFromCode = stuFromCode;
	}
}
