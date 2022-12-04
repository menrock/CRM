package com.niu.crm.vo;

import java.io.Serializable;
import java.util.Date;

public class RepeatCustVO implements Serializable {
	private static final long serialVersionUID = 1685172323641833636L;
	
	private Boolean precust;
	private Long stuId;  //stuId
	
	private Long cstmId;
	private String cstmName;
	private Long stuFromId;
	private String stuFromName;
	private String mobile;
	
	private Long companyId;
	private String companyName;
	private Long creatorId;
	private Date createdAt;
	
	public Boolean getPrecust() {
		return precust;
	}
	public void setPrecust(Boolean precust) {
		this.precust = precust;
	}
	
	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	
	public Long getCstmId() {
		return cstmId;
	}
	public void setCstmId(Long cstmId) {
		this.cstmId = cstmId;
	}
	
	public String getCstmName() {
		return cstmName;
	}
	public void setCstmName(String cstmName) {
		this.cstmName = cstmName;
	}
	
	public Long getStuFromId() {
		return stuFromId;
	}
	public void setStuFromId(Long stuFromId) {
		this.stuFromId = stuFromId;
	}
	
	public String getStuFromName() {
		return stuFromName;
	}
	public void setStuFromName(String stuFromName) {
		this.stuFromName = stuFromName;
	}
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
