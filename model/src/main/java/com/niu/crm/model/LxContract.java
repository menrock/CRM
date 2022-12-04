package com.niu.crm.model;


import java.util.Date;

import com.niu.crm.model.type.LxContractStatus;

/** 
 * @author seker(seker@ixiaopu.com)
 * @date 2017年3月29日
 */
public class LxContract extends AbstractBizContract {

	/**
	 * 
	 */
	private static final long serialVersionUID = 512113118887384818L;
	
	private Long conType;
	private String countryCodes;
	private String countryNames;

	private Integer planEnterYear;   //计划入学年份  
	private String planEnterSeason;  //计划入学季节
	
	private Long signGwId;
	private Long planGwId;
	private Long applyGwId;
	private Long writeGwId;
	private Long serviceGwId;
	private LxContractStatus status;
	private Integer tranFlag; //转后期标记 (0 未转， 1已提交转案申请, 2已分配后期顾问)
	private Date tranSubmitTime;
	private Date tranDistTime;
	
	
	public Long getConType() {
		return conType;
	}
	public void setConType(Long conType) {
		this.conType = conType;
	}

	public String getCountryCodes() {
		return countryCodes;
	}
	public void setCountryCodes(String countryCodes) {
		this.countryCodes = countryCodes;
	}
	
	public String getCountryNames() {
		return countryNames;
	}
	public void setCountryNames(String countryNames) {
		this.countryNames = countryNames;
	}
	
	public Long getSignGwId() {
		return signGwId;
	}
	public void setSignGwId(Long signGwId) {
		this.signGwId = signGwId;
	}
	
	public Long getApplyGwId() {
		return applyGwId;
	}
	public void setApplyGwId(Long applyGwId) {
		this.applyGwId = applyGwId;
	}
	
	public Long getWriteGwId() {
		return writeGwId;
	}
	public void setWriteGwId(Long writeGwId) {
		this.writeGwId = writeGwId;
	}
	
	public Long getServiceGwId() {
		return serviceGwId;
	}
	public void setServiceGwId(Long serviceGwId) {
		this.serviceGwId = serviceGwId;
	}
	
	public Long getPlanGwId() {
		return planGwId;
	}
	public void setPlanGwId(Long planGwId) {
		this.planGwId = planGwId;
	}
	
	public String getStatusName() {
		if(status == null)
			return null;
		else
			return status.getName();
	}
	public LxContractStatus getStatus() {
		return status;
	}
	public void setStatus(LxContractStatus status) {
		this.status = status;
	}
	
	public Integer getPlanEnterYear() {
		return planEnterYear;
	}
	public void setPlanEnterYear(Integer planEnterYear) {
		this.planEnterYear = planEnterYear;
	}
	
	public String getPlanEnterSeason() {
		return planEnterSeason;
	}
	public void setPlanEnterSeason(String planEnterSeason) {
		this.planEnterSeason = planEnterSeason;
	}
	
	public Integer getTranFlag() {
		return tranFlag;
	}
	public void setTranFlag(Integer tranFlag) {
		this.tranFlag = tranFlag;
	}
	
	public Date getTranSubmitTime() {
		return tranSubmitTime;
	}
	public void setTranSubmitTime(Date tranSubmitTime) {
		this.tranSubmitTime = tranSubmitTime;
	}
	
	public Date getTranDistTime() {
		return tranDistTime;
	}
	public void setTranDistTime(Date tranDistTime) {
		this.tranDistTime = tranDistTime;
	}
}
