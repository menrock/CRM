package com.niu.crm.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.niu.crm.model.type.CommInvoiceStatus;

public class CommInvoice extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4000776445606861498L;
	
	private Integer invNo;
	private Long collegeId;
	private Long orgId;
	private String objectName;
	private String invCurrency;
	private BigDecimal invAmount;
	private Date sentDate;
	private Date receivedDate;
	private BigDecimal receivedAmount;
	private CommInvoiceStatus status;
	private Long creatorId;
	private Date updatedAt;
	
	private List<CommInvoiceLine> lines;
	
	public Integer getInvNo() {
		return invNo;
	}
	public void setInvNo(Integer invNo) {
		this.invNo = invNo;
	}
	
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
	
	public String getInvCurrency() {
		return invCurrency;
	}
	public void setInvCurrency(String invCurrency) {
		this.invCurrency = invCurrency;
	}
	
	public BigDecimal getInvAmount() {
		return invAmount;
	}
	public void setInvAmount(BigDecimal invAmount) {
		this.invAmount = invAmount;
	}
	
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	
	public BigDecimal getReceivedAmount() {
		return receivedAmount;
	}
	public void setReceivedAmount(BigDecimal receivedAmount) {
		this.receivedAmount = receivedAmount;
	}
	
	public String getStatusName() {
		if(status !=null)
			return status.getName();
		else
			return null;
	}
	public CommInvoiceStatus getStatus() {
		return status;
	}
	public void setStatus(CommInvoiceStatus status) {
		this.status = status;
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
	
	public List<CommInvoiceLine> getLines() {
		return lines;
	}
	public void setLines(List<CommInvoiceLine> lines) {
		this.lines = lines;
	}
}
