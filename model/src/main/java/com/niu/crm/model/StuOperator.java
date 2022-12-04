package com.niu.crm.model;

import java.util.Date;

public class StuOperator extends BaseModel{
	private static final long serialVersionUID = 2662323737736457626L;
	
	//按位控制 如果 acl=0 不再可访问(按位控制)
	public static final long ACL_CREATOR=0x1;
	public static final long ACL_ZXGW=0x2;
	public static final long ACL_SHARED=0x4;
	public static final long ACL_OWNER=0x8;
		
	private Long stuId;
	private Long operatorId;
	private Long acl;
	private Boolean isZxgw;
	private Date assignDate;
	private Long stuLevel;
	private Date updatedAt;
	
	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	public Long getOperatorId() {
		return operatorId;
	}
	
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	
	public Long getAcl() {
		return acl;
	}
	public void setAcl(Long acl) {
		this.acl = acl;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Boolean getIsZxgw() {
		return isZxgw;
	}
	public void setIsZxgw(Boolean isZxgw) {
		this.isZxgw = isZxgw;
	}
	public Date getAssignDate() {
		return assignDate;
	}
	public void setAssignDate(Date assignDate) {
		this.assignDate = assignDate;
	}
	public Long getStuLevel() {
		return stuLevel;
	}
	public void setStuLevel(Long stuLevel) {
		this.stuLevel = stuLevel;
	}
}