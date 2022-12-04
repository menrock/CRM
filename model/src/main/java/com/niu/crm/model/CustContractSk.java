package com.niu.crm.model;

import java.math.BigDecimal;
import java.util.Date;

import com.niu.crm.model.type.SkStatus;

public class CustContractSk extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1530445063535167894L;
	
	private Long companyId;
	private Long cstmId;
	private Long conId;
	private Long payLineId;  // t_payment_line.id
	private Long payType;
	private String payTypeName;
	private Date skDate;
	private Long itemId;
	private BigDecimal skValue;
	private BigDecimal achivement; //业绩
	private String kcgwName;
	private String memo;
	private SkStatus status;
	private Long creatorId;
	private Date updatedAt;
	

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
	
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	
	public Long getPayLineId() {
		return payLineId;
	}
	public void setPayLineId(Long payLineId) {
		this.payLineId = payLineId;
	}
	
	public Date getSkDate() {
		return skDate;
	}
	public void setSkDate(Date skDate) {
		this.skDate = skDate;
	}
	
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	
	public BigDecimal getSkValue() {
		return skValue;
	}
	public void setSkValue(BigDecimal skValue) {
		this.skValue = skValue;
	}

	public BigDecimal getAchivement() {
		return achivement;
	}
	public void setAchivement(BigDecimal achivement) {
		this.achivement = achivement;
	}
	
	public SkStatus getStatus() {
		return status;
	}
	public void setStatus(SkStatus status) {
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
	public String getKcgwName() {
		return kcgwName;
	}
	public void setKcgwName(String kcgwName) {
		this.kcgwName = kcgwName;
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
