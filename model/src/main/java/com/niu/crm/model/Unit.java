package com.niu.crm.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Unit extends BaseModel{
	private static final long serialVersionUID = 8254338342058057593L;
	
	private Long showIndex;
	private String alias;
	private String code;
	private String name;
	private Long parentId;
	private Long companyId;
	private Long creatorId;
	private Date updatedAt;
	
	private Long wxUnitId; //微信中的unitId

	public Long getShowIndex() {
		return showIndex;
	}
	public void setShowIndex(Long showIndex) {
		this.showIndex = showIndex;
	}
	
	public String getAlias() {
		if(alias !=null || code ==null)
			return alias;
		
		int index = code.lastIndexOf(".");
		if(index >0)
			return code.substring(index +1);
		else
			return code;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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
	public Long getWxUnitId() {
		return wxUnitId;
	}
	public void setWxUnitId(Long wxUnitId) {
		this.wxUnitId = wxUnitId;
	}
}