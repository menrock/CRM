package com.niu.crm.form;

import java.util.Date;
import java.util.List;

import com.niu.crm.model.type.ApproveStatus;

public class LxContractTranApplySearchForm {
	private Integer tranType;
	private Long companyId;
	private Long lxConId;
	private String cstmName;
	private String conNo;
	private List<ApproveStatus> statusList;
	private Date submitFrom ;
	private Date submitTo ;
	private Long creatorId;
	
	private String aclClause;
	
	//是否是审批查询
	private Boolean approveQuery;

	public Integer getTranType() {
		return tranType;
	}
	public void setTranType(Integer tranType) {
		this.tranType = tranType;
	}

	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCstmName() {
		return cstmName;
	}
	public void setCstmName(String cstmName) {
		this.cstmName = cstmName;
	}

	public Long getLxConId() {
		return lxConId;
	}
	public void setLxConId(Long lxConId) {
		this.lxConId = lxConId;
	}

	public String getConNo() {
		return conNo;
	}
	public void setConNo(String conNo) {
		this.conNo = conNo;
	}

	public List<ApproveStatus> getStatusList() {
		return statusList;
	}
	public void setStatusList(List<ApproveStatus> statusList) {
		this.statusList = statusList;
	}

	public Date getSubmitFrom() {
		return submitFrom;
	}
	public void setSubmitFrom(Date submitFrom) {
		this.submitFrom = submitFrom;
	}

	public Date getSubmitTo() {
		return submitTo;
	}
	public void setSubmitTo(Date submitTo) {
		this.submitTo = submitTo;
	}

	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	
	public String getAclClause() {
		return aclClause;
	}
	public void setAclClause(String aclClause) {
		this.aclClause = aclClause;
	}
	
	public Boolean getApproveQuery() {
		return approveQuery;
	}
	public void setApproveQuery(Boolean approveQuery) {
		this.approveQuery = approveQuery;
	}
}
