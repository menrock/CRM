package com.niu.crm.model;

import java.util.Date;

import com.niu.crm.model.type.ApproveStatus;

public class LxContractTranApply extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4847271032347353915L;
	
	
	//1 普通转顾问  2转后期
	private Integer tranType;
	private Long lxConId;
	private Long fromPlanGwId;
	private Long toPlanGwId;
	private Long fromApplyGwId;
	private Long toApplyGwId;
	private Long fromWriteGwId;
	private Long toWriteGwId;
	private Long fromServiceGwId;
	private Long toServiceGwId;
	private String applyMemo;
	
	
	private Date submitTime;
	private Date approvedTime;
	private Long approverId;
	private String approverName;
	private ApproveStatus status;
	private Long creatorId;
	private Date updatedAt;
	

	public Integer getTranType() {
		return tranType;
	}
	public void setTranType(Integer tranType) {
		this.tranType = tranType;
	}
	
	public Long getLxConId() {
		return lxConId;
	}
	public void setLxConId(Long lxConId) {
		this.lxConId = lxConId;
	}
	
	public Long getFromPlanGwId() {
		return fromPlanGwId;
	}
	public void setFromPlanGwId(Long fromPlanGwId) {
		this.fromPlanGwId = fromPlanGwId;
	}
	
	public Long getToPlanGwId() {
		return toPlanGwId;
	}
	public void setToPlanGwId(Long toPlanGwId) {
		this.toPlanGwId = toPlanGwId;
	}
	
	public Long getFromApplyGwId() {
		return fromApplyGwId;
	}
	public void setFromApplyGwId(Long fromApplyGwId) {
		this.fromApplyGwId = fromApplyGwId;
	}
	
	public Long getToApplyGwId() {
		return toApplyGwId;
	}
	public void setToApplyGwId(Long toApplyGwId) {
		this.toApplyGwId = toApplyGwId;
	}
	
	public Long getFromWriteGwId() {
		return fromWriteGwId;
	}
	public void setFromWriteGwId(Long fromWriteGwId) {
		this.fromWriteGwId = fromWriteGwId;
	}
	
	public Long getToWriteGwId() {
		return toWriteGwId;
	}
	public void setToWriteGwId(Long toWriteGwId) {
		this.toWriteGwId = toWriteGwId;
	}
	
	public Long getFromServiceGwId() {
		return fromServiceGwId;
	}
	public void setFromServiceGwId(Long fromServiceGwId) {
		this.fromServiceGwId = fromServiceGwId;
	}
	
	public Long getToServiceGwId() {
		return toServiceGwId;
	}
	public void setToServiceGwId(Long toServiceGwId) {
		this.toServiceGwId = toServiceGwId;
	}

	public String getApplyMemo() {
		return applyMemo;
	}
	public void setApplyMemo(String applyMemo) {
		this.applyMemo = applyMemo;
	}
	
	public Date getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Long getApproverId() {
		return approverId;
	}
	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}

	public String getApproverName() {
		return approverName;
	}
	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
	
	public Date getApprovedTime() {
		return approvedTime;
	}
	public void setApprovedTime(Date approveTime) {
		this.approvedTime = approveTime;
	}
	
	public String getStatusName() {
		if(status == null)
			return null;
		else
			return status.getName();
	}
	public ApproveStatus getStatus() {
		return status;
	}
	public void setStatus(ApproveStatus status) {
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
}
