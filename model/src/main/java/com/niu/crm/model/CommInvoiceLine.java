package com.niu.crm.model;

import java.math.BigDecimal;
import java.util.Date;

public class CommInvoiceLine extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1116779233223233791L;
	
	private Long invId;
	private Long companyId;
	private Long contractId;
	private String stuName;
	private Date stuBirth;
	private String courseName;
	private String courseTerm;
	private BigDecimal courseFee;
	private BigDecimal commRatio;
	private BigDecimal commAmount;
	private Boolean lastOne;
	private String memo;
	
	
	public Long getInvId() {
		return invId;
	}
	public void setInvId(Long invId) {
		this.invId = invId;
	}
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	public Long getContractId() {
		return contractId;
	}
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}
	
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	
	public Date getStuBirth() {
		return stuBirth;
	}
	public void setStuBirth(Date stuBirth) {
		this.stuBirth = stuBirth;
	}
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	public String getCourseTerm() {
		return courseTerm;
	}
	public void setCourseTerm(String courseTerm) {
		this.courseTerm = courseTerm;
	}
	public BigDecimal getCourseFee() {
		return courseFee;
	}
	public void setCourseFee(BigDecimal courseFee) {
		this.courseFee = courseFee;
	}
	public BigDecimal getCommRatio() {
		return commRatio;
	}
	public void setCommRatio(BigDecimal commRatio) {
		this.commRatio = commRatio;
	}
	public BigDecimal getCommAmount() {
		return commAmount;
	}
	public void setCommAmount(BigDecimal commAmount) {
		this.commAmount = commAmount;
	}
	public Boolean getLastOne() {
		return lastOne;
	}
	public void setLastOne(Boolean lastOne) {
		this.lastOne = lastOne;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	

}
