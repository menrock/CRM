package com.niu.crm.model;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentLine extends BaseModel{
	private static final long serialVersionUID = 8953203091620405471L;
	
	private Long payId;
	private Long companyId;
	private Long conId;
	private Long itemId;
	private BigDecimal itemValue;
	private String kcgwName; //课程顾问姓名
	private String memo;
	private Long creatorId;

	public Long getPayId() {
		return payId;
	}
	public void setPayId(Long payId) {
		this.payId = payId;
	}
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getItemValue() {
		return itemValue;
	}
	public void setItemValue(BigDecimal itemValue) {
		this.itemValue = itemValue;
	}
	
	public String getKcgwName() {
		return kcgwName;
	}
	public void setKcgwName(String kcgwName) {
		this.kcgwName = kcgwName;
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
}
