package com.niu.crm.form;

import java.util.Date;

import com.niu.crm.model.type.CommInvoiceStatus;

public class CommInvoiceSearchForm {
	private Long collegeId;
	private Long orgId;
	private String objectName;
	private Date sentDateFrom;
	private Date sentDateTo;

	private Date receivedDateFrom;
	private Date receivedDateTo;
	
	private CommInvoiceStatus status;

	public Long getCollegeId() {
		return collegeId;
	}
	public void setCollegeId(Long collegeId) {
		this.collegeId = collegeId;
	}

	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public Date getSentDateFrom() {
		return sentDateFrom;
	}
	public void setSentDateFrom(Date sentDateFrom) {
		this.sentDateFrom = sentDateFrom;
	}

	public Date getSentDateTo() {
		return sentDateTo;
	}
	public void setSentDateTo(Date sentDateTo) {
		this.sentDateTo = sentDateTo;
	}

	public Date getReceivedDateFrom() {
		return receivedDateFrom;
	}
	public void setReceivedDateFrom(Date receivedDateFrom) {
		this.receivedDateFrom = receivedDateFrom;
	}
	
	public Date getReceivedDateTo() {
		return receivedDateTo;
	}
	public void setReceivedDateTo(Date receivedDateTo) {
		this.receivedDateTo = receivedDateTo;
	}

	public CommInvoiceStatus getStatus() {
		return status;
	}
	public void setStatus(CommInvoiceStatus status) {
		this.status = status;
	}
}
