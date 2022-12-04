package com.niu.crm.form;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.niu.crm.model.type.PaymentStatus;

public class PaymentSearchForm {
	private Long cstmId;

	private Long companyId;
	private String cstmName;
	private String payNo;
	private String billNo;
	private String payType;
	private String payerName;
	private Date paidFrom;
	private Date paidTo;
	private String creatorName;
	private List<PaymentStatus> statusList;
	private Boolean fuzzySearch;
	
	private String aclClause;
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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
		if(StringUtils.isEmpty(cstmName))
			this.cstmName = null;
		else
			this.cstmName = cstmName;
	}
	
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		if(StringUtils.isEmpty(payNo))
			this.payNo = null;
		else
			this.payNo = payNo;
	}
	
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		if(StringUtils.isEmpty(billNo))
			this.billNo = null;
		else
			this.billNo = billNo;
	}
	
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		if(StringUtils.isEmpty(payType))
			this.payType = null;
		else
			this.payType = payType;
	}
	
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		if(StringUtils.isEmpty(payerName))
			this.payerName = null;
		else
			this.payerName = payerName;
	}
	
	public Date getPaidFrom() {
		return paidFrom;
	}
	public void setPaidFrom(Date paidFrom) {
		this.paidFrom = paidFrom;
	}
	
	public Date getPaidTo() {
		return paidTo;
	}
	public void setPaidTo(Date paidTo) {
		this.paidTo = paidTo;
	}
	
	public List<PaymentStatus> getStatusList() {
		return statusList;
	}
	public void setStatusList(List<PaymentStatus> statusList) {
		this.statusList = statusList;
	}

	public String getAclClause() {
		return aclClause;
	}
	public void setAclClause(String aclClause) {
		this.aclClause = aclClause;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		if(StringUtils.isBlank(creatorName))
			this.creatorName = null;
		else
			this.creatorName = creatorName;
	}
	public Boolean getFuzzySearch() {
		return fuzzySearch;
	}
	public void setFuzzySearch(Boolean fuzzySearch) {
		this.fuzzySearch = fuzzySearch;
	}
}
