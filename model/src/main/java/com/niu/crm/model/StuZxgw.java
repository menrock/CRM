package com.niu.crm.model;

import java.util.Date;

public class StuZxgw extends BaseModel{
	private static final long serialVersionUID = 5689135276639419301L;
	
	private Long stuId;
	private Long zxgwId;
	private Long stuLevel;
	private Date assignDate;
	private Integer contactCount;
	private Date lastContactDate; //最近联系时间
	private Long creatorId;
	
	private String zxgwName;
	private String stuLevelName;
	
	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	
	public Long getZxgwId() {
		return zxgwId;
	}	
	public void setZxgwId(Long zxgwId) {
		this.zxgwId = zxgwId;
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
	
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	public Integer getContactCount() {
		return contactCount;
	}
	public void setContactCount(Integer contactCount) {
		this.contactCount = contactCount;
	}
	public Date getLastContactDate() {
		return lastContactDate;
	}
	public void setLastContactDate(Date lastContactDate) {
		this.lastContactDate = lastContactDate;
	}
	public String getZxgwName() {
		return zxgwName;
	}
	public void setZxgwName(String zxgwName) {
		this.zxgwName = zxgwName;
	}
	public String getStuLevelName() {
		return stuLevelName;
	}
	public void setStuLevelName(String stuLevelName) {
		this.stuLevelName = stuLevelName;
	}
}