package com.niu.crm.model;

import java.math.BigDecimal;
import java.util.Date;

import com.niu.crm.model.type.PaymentStatus;

public class Payment extends BaseModel{
	private static final long serialVersionUID = 8924332288594878906L;
	
	private Long companyId;
	private String payNo;
	private String payerName;
	private String bankName;
	private String billNo;
	private Long cstmId;
	private BigDecimal invMoney;
	private BigDecimal paidMoney;
	private BigDecimal skValue;
	private Long payType;
	private String payTypeName;
	private Date paidAt;
	private PaymentStatus status;
	private String memo;
	private Long creatorId;
	private Date updatedAt;
	
	private Long fzConfirmerId;
	private Date fzConfirmedAt;

	private Long confirmerId;
	private Date confirmedAt;
	

	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
		
	public String getPayNo() {
		return payNo;
	}	
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBillNo() {
		return billNo;
	}
	
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public Long getCstmId() {
		return cstmId;
	}
	
	public void setCstmId(Long cstmId) {
		this.cstmId = cstmId;
	}
	public BigDecimal getInvMoney() {
		return invMoney;
	}
	
	public void setInvMoney(BigDecimal invMoney) {
		this.invMoney = invMoney;
	}
	public BigDecimal getPaidMoney() {
		return paidMoney;
	}
	
	public void setPaidMoney(BigDecimal paidMoney) {
		this.paidMoney = paidMoney;
	}
	public BigDecimal getSkValue() {
		return skValue;
	}
	public void setSkValue(BigDecimal skValue) {
		this.skValue = skValue;
	}
	
	public Date getPaidAt() {
		return paidAt;
	}
	public void setPaidAt(Date paidAt) {
		this.paidAt = paidAt;
	}
	
	public String getStatusName() {
		if(status == null)
			return null;
		else
			return status.getName();
	}
	public PaymentStatus getStatus() {
		return status;
	}
	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	
	public Long getFzConfirmerId() {
		return fzConfirmerId;
	}
	public void setFzConfirmerId(Long fzConfirmerId) {
		this.fzConfirmerId = fzConfirmerId;
	}
	
	public Date getFzConfirmedAt() {
		return fzConfirmedAt;
	}
	public void setFzConfirmedAt(Date fzConfirmedAt) {
		this.fzConfirmedAt = fzConfirmedAt;
	}
	
	public Long getConfirmerId() {
		return confirmerId;
	}
	public void setConfirmerId(Long confirmerId) {
		this.confirmerId = confirmerId;
	}
	
	public Date getConfirmedAt() {
		return confirmedAt;
	}
	public void setConfirmedAt(Date confirmedAt) {
		this.confirmedAt = confirmedAt;
	}
	
	public Long getPayType() {
		return payType;
	}
	public void setPayType(Long payType) {
		this.payType = payType;
	}
	
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
}
