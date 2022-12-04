package com.niu.crm.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.niu.crm.model.CustContract;
import com.niu.crm.model.CustContractLine;
import com.niu.crm.model.CustContractSk;

public class CustContractVO extends CustContract{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8165672970874779033L;
	
	private String companyName;
	
	private String cstmName;
	private String conTypeName;
	private Long stuFromId;
	private String stuFromName;
	private String currSchool; //毕业/在读学校
	private String stuCity; //学生所在城市/地区
	
	private String signGwName;
	private String planGwName;
	private String applyGwName;
	private String writeGwName;
	private String serviceGwName;
	
	private List<CustContractSk> skLines = null;
	private List<CustContractLine> feeLines = null;
		
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

	public String getConTypeName() {
		return conTypeName;
	}
	public void setConTypeName(String conTypeName) {
		this.conTypeName = conTypeName;
	}
	
	public String getStuCity() {
		return stuCity;
	}
	public void setStuCity(String stuCity) {
		this.stuCity = stuCity;
	}
	
	public List<CustContractSk> getSkLines() {
		return skLines;
	}
	public void setSkLines(List<CustContractSk> skLines) {
		this.skLines = skLines;
	}
	
	public List<CustContractLine> getFeeLines() {
		return feeLines;
	}
	public void setFeeLines(List<CustContractLine> feeLines) {
		this.feeLines = feeLines;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSignGwName() {
		return signGwName;
	}
	public void setSignGwName(String signGwName) {
		this.signGwName = signGwName;
	}
	public String getPlanGwName() {
		return planGwName;
	}
	public void setPlanGwName(String planGwName) {
		this.planGwName = planGwName;
	}
	public String getWriteGwName() {
		return writeGwName;
	}
	public void setWriteGwName(String writeGwName) {
		this.writeGwName = writeGwName;
	}
	public String getApplyGwName() {
		return applyGwName;
	}
	public void setApplyGwName(String applyGwName) {
		this.applyGwName = applyGwName;
	}
	public String getServiceGwName() {
		return serviceGwName;
	}
	public void setServiceGwName(String serviceGwName) {
		this.serviceGwName = serviceGwName;
	}
	public String getCurrSchool() {
		return currSchool;
	}
	public void setCurrSchool(String currSchool) {
		this.currSchool = currSchool;
	}	
}
