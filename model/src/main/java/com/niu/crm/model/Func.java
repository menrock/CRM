package com.niu.crm.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.niu.crm.model.type.AclScope;

public class Func extends BaseModel{
	private static final long serialVersionUID = -9135010919774441159L;
	
	private Long showIndex;
	private String code;
	private String name;
	private String funcDesc;
	private String aclScopes;
	private Long creatorId;
	private Date updatedAt;

	public Long getShowIndex() {
		return showIndex;
	}
	public void setShowIndex(Long showIndex) {
		this.showIndex = showIndex;
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
	public void setDictName(String name) {
		this.name = name;
	}
	
	public String getFuncDesc() {
		return funcDesc;
	}
	public void setFuncDesc(String funcDesc) {
		this.funcDesc = funcDesc;
	}

	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	public String getAclScopes() {
		return aclScopes;
	}
	public void setAclScopes(String aclScopes) {
		this.aclScopes = aclScopes;
	}
	
	public boolean inScopes(AclScope aclScope){
		if(StringUtils.isEmpty(aclScopes))
			return false;
		
		String[] arrScope = aclScopes.split(",");
		for(String s:arrScope){
			if(s.equals(aclScope.toString()))
				return true;
		}
		return false;		
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}