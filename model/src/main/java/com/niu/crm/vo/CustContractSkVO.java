package com.niu.crm.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.niu.crm.model.CustContractSk;

public class CustContractSkVO extends CustContractSk{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2415215739526670282L;

	
	private String companyName;
	private Long conType;
	private String conTypeName;
	private String conTypeName1; //合同大类
	private String conNo;
	private String itemName;
	private String cstmName;
	private Long stuFromId;
	//一级客户来源
	private String stuFromName1;
	private String stuFromName;
	
	private Integer planEnterYear;
	private String planEnterSeason;
	
	private Long signGwId;
	private String signGwName;
	private Long planGwId;
	private String planGwName;
	private Long applyGwId;
	private String applyGwName;
	private Long writeGwId;
	private String writeGwName;
	private Long serviceGwId;
	private String serviceGwName;
	private String countryCodes;
	private String countryNames;

	public String getConNo() {
		return conNo;
	}
	public void setConNo(String conNo) {
		this.conNo = conNo;
	}

	public String getCstmName() {
		return cstmName;
	}
	public void setCstmName(String cstmName) {
		this.cstmName = cstmName;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Long getSignGwId() {
		return signGwId;
	}
	public void setSignGwId(Long signGwId) {
		this.signGwId = signGwId;
	}
	public String getSignGwName() {
		return signGwName;
	}
	public void setSignGwName(String signGwName) {
		this.signGwName = signGwName;
	}
	public Long getPlanGwId() {
		return planGwId;
	}
	public void setPlanGwId(Long planGwId) {
		this.planGwId = planGwId;
	}
	public String getPlanGwName() {
		return planGwName;
	}
	public void setPlanGwName(String planGwName) {
		this.planGwName = planGwName;
	}
	public Long getApplyGwId() {
		return applyGwId;
	}
	public void setApplyGwId(Long applyGwId) {
		this.applyGwId = applyGwId;
	}
	public String getApplyGwName() {
		return applyGwName;
	}
	public void setApplyGwName(String applyGwName) {
		this.applyGwName = applyGwName;
	}
	public Long getWriteGwId() {
		return writeGwId;
	}
	public void setWriteGwId(Long writeGwId) {
		this.writeGwId = writeGwId;
	}
	public String getWriteGwName() {
		return writeGwName;
	}
	public void setWriteGwName(String writeGwName) {
		this.writeGwName = writeGwName;
	}
	public Long getServiceGwId() {
		return serviceGwId;
	}
	public void setServiceGwId(Long serviceGwId) {
		this.serviceGwId = serviceGwId;
	}
	public String getServiceGwName() {
		return serviceGwName;
	}
	public void setServiceGwName(String serviceGwName) {
		this.serviceGwName = serviceGwName;
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
	public Long getConType() {
		return conType;
	}
	public void setConType(Long conType) {
		this.conType = conType;
	}
	
	public String getConTypeName() {
		return conTypeName;
	}
	public void setConTypeName(String conTypeName) {
		this.conTypeName = conTypeName;
	}
	
	public String getConTypeName1() {
		return conTypeName1;
	}
	public void setConTypeName1(String conTypeName1) {
		this.conTypeName1 = conTypeName1;
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
	public Long getStuFromId() {
		return stuFromId;
	}
	public void setStuFromId(Long stuFromId) {
		this.stuFromId = stuFromId;
	}
	

	public String getStuFromName1() {
		return stuFromName1;
	}
	public void setStuFromName1(String stuFromName1) {
		this.stuFromName1 = stuFromName1;
	}
	
	public String getStuFromName() {
		return stuFromName;
	}
	public void setStuFromName(String stuFromName) {
		this.stuFromName = stuFromName;
	}
}
