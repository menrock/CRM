package com.niu.crm.model;

import java.util.Date;

public class CustPhone extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1530445063535167894L;
	
	private Long cstmId;
	private String showPhone;
	private String phone;
	private Boolean isMain;
	private String memo;
	private Long updatorId;
	private Date updatedAt;
	
	public Long getCstmId() {
		return cstmId;
	}
	public void setCstmId(Long cstmId) {
		this.cstmId = cstmId;
	}
	
	public String getShowPhone() {
		return showPhone;
	}
	public void setShowPhone(String showPhone) {
		this.showPhone = showPhone;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Boolean getIsMain() {
		return isMain;
	}
	public void setIsMain(Boolean isMain) {
		this.isMain = isMain;
	}
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public Long getUpdatorId() {
		return updatorId;
	}
	public void setUpdatorId(Long updatorId) {
		this.updatorId = updatorId;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
}
