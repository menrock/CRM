package com.niu.crm.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.niu.crm.model.type.PreCustStatus;



public class LxPreCust extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8397212521362352591L;
	
	private Long cstmId;
	private Long companyId;  //归属公司
	private String companyName;
	
	private String visitorType;
	private String gpa; 

	private String currSchool;  //在读学校
	private String currGrade;  //在读年级
	private String stuXl;  //当前学历
	private String planXl;        //申请学历(计划申请)
	private String planCountry;      //意向国家
	private String planCountryName;
	
	private Integer planEnterYear;   //计划入学年份  
	private String planEnterSeason;  //计划入学季节
		
	private String currentSpecialty;  //当前专业
	private String hopeSpecialty;  //意向专业
	private Long stuFromId;
	private String stuFromName;
	
	
	private String pxRequire; //培训需求
	
	private String stuCity;  //所在城市
	private String basicInfo;
	
	private Integer contactCount;
	private Date lastContactDate;
	
	private String memo;
	
	private PreCustStatus status;
	private String statusName;
	private Long creatorId;
	private Date updatedAt;

	public Long getCstmId() {
		return cstmId;
	}
	public void setCstmId(Long cstmId) {
		this.cstmId = cstmId;
	}

	
	public String getVisitorType() {
		return visitorType;
	}
	public void setVisitorType(String visitorType) {
		this.visitorType = visitorType;
	}
	
	public String getGpa() {
		return gpa;
	}
	public void setGpa(String gpa) {
		this.gpa = gpa;
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

	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	public String getCurrentSpecialty() {
		return currentSpecialty;
	}
	public void setCurrentSpecialty(String currentSpecialty) {
		this.currentSpecialty = currentSpecialty;
	}
	
	public String getHopeSpecialty() {
		return hopeSpecialty;
	}
	public void setHopeSpecialty(String hopeSpecialty) {
		this.hopeSpecialty = hopeSpecialty;
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
		
	public String getCurrSchool() {
		return currSchool;
	}
	public void setCurrSchool(String currSchool) {
		this.currSchool = currSchool;
	}
	
	public String getCurrGrade() {
		return currGrade;
	}
	public void setCurrGrade(String currGrade) {
		this.currGrade = currGrade;
	}
	
	public String getStuXl() {
		return stuXl;
	}
	public void setStuXl(String stuXl) {
		this.stuXl = stuXl;
	}
	
	public String getPlanXl() {
		return planXl;
	}
	public void setPlanXl(String planXl) {
		this.planXl = planXl;
	}
	
	public String getPlanCountry() {
		return planCountry;
	}
	public void setPlanCountry(String planCountry) {
		this.planCountry = planCountry;
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
	
	public String getStuCity() {
		return stuCity;
	}
	public void setStuCity(String stuCity) {
		this.stuCity = stuCity;
	}
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public String getPxRequire() {
		return pxRequire;
	}
	public void setPxRequire(String pxRequire) {
		this.pxRequire = pxRequire;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPlanCountryName() {
		return planCountryName;
	}
	public void setPlanCountryName(String planCountryName) {
		this.planCountryName = planCountryName;
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
	

	public String getStatusName() {
		if(status == null)
			return null;
		else
			return status.getName();
	}
	public PreCustStatus getStatus() {
		return status;
	}
	public void setStatus(PreCustStatus status) {
		this.status = status;
	}
	
	public String getBasicInfo() {
		return basicInfo;
	}
	public void setBasicInfo(String basicInfo) {
		this.basicInfo = basicInfo;
	}
}