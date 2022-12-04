package com.niu.crm.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CoopOrg extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8524914493109458190L;
	
	
	private Long showIndex;
	private String enName;
	private String cnName;
	private String abbrName;
	private Long creatorId;
	private Date updatedAt;

	public Long getShowIndex() {
		return showIndex;
	}
	public void setShowIndex(Long showIndex) {
		this.showIndex = showIndex;
	}
	

	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getAbbrName() {
		return abbrName;
	}
	public void setAbbrName(String abbrName) {
		this.abbrName = abbrName;
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