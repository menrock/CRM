package com.niu.crm.model;

import com.niu.crm.model.type.AclScope;

public class UserFunc extends BaseModel{
	private static final long serialVersionUID = -8001665143818467497L;
	
	private Long userId;
	private Long funcId;
	private String funcCode;
	private String funcName;
	private AclScope aclScope;
	private String unitIds;
	private String companyIds;
	private String fromIds;
	private String clause;  //个性化定制限制条件 此属性不为空时 unitIds、companyids、fromIds 失效
	
	private Long creatorId;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getFuncId() {
		return funcId;
	}
	public void setFuncId(Long funcId) {
		this.funcId = funcId;
	}

	public String getFuncCode() {
		return funcCode;
	}
	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}

	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	
	public AclScope getAclScope() {
		return aclScope;
	}
	public void setAclScope(AclScope aclScope) {
		this.aclScope = aclScope;
	}

	public String getUnitIds() {
		return unitIds;
	}
	public void setUnitIds(String unitIds) {
		this.unitIds = unitIds;
	}

	public String getCompanyIds() {
		return companyIds;
	}
	public void setCompanyIds(String companyIds) {
		this.companyIds = companyIds;
	}

	public String getFromIds() {
		return fromIds;
	}
	public void setFromIds(String fromIds) {
		this.fromIds = fromIds;
	}

	public String getClause() {
		return clause;
	}
	public void setClause(String clause) {
		this.clause = clause;
	}
	
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
}