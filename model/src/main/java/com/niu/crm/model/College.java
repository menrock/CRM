package com.niu.crm.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

public class College extends BaseModel{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8451966291144730037L;
	
	private Long showIndex;
	private String cnName;
	private String enName;
	private String level;
	private String colType;
	private String countryCode;
	private String countryName;
	private Long creatorId;
	private Date updatedAt;

	public Long getShowIndex() {
		return showIndex;
	}
	public void setShowIndex(Long showIndex) {
		this.showIndex = showIndex;
	}
	
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}

	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
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