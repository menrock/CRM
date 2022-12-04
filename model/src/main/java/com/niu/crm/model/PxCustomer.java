package com.niu.crm.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PxCustomer extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3446067942126056830L;
	
	private Long cstmId;
	private Long unitId;     //归属部门
	private String unitName;
	private Long companyId;  //归属公司
	private String companyName;
	
	private Date inquireDate;  //咨询日期
	private Date visitDate;     //来访日期
	private String visitorType;
	private String gpa; 
	private String basicInfo;
	private String lastInfo;

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
	private String zxgwNames;
	
	private String flToefl;
	private String flToeflJunior;
	private String flIelts;
	private String flGre;
	private String flGmat;
	private String flSat;
	private String flSat2;
	private String flSsat;
	private String flAct;
	
	//最高评级
	private Long stuLevel;
	private String stuLevelName;
	
	private Date firstAssignDate;  //首次分配咨询顾问时间
	private String callCenter; //callCenter进展
	private String stuCity;  //所在城市
	private String toCity;   //投放城市
	private Long toCityId;   //投放城市Id
	
	private Integer contactCount;
	private Date lastContactDate; //最近联系时间
	private Date signDate;  //首次签约日期
	private String memo;
	private Long ownerId;
	private String ownerName;
	
	private Long creatorId;
	private Date updatedAt;
	private Boolean archive;

	public Long getCstmId() {
		return cstmId;
	}
	public void setCstmId(Long cstmId) {
		this.cstmId = cstmId;
	}
	
	public Date getInquireDate() {
		return inquireDate;
	}
	public void setInquireDate(Date inquireDate) {
		this.inquireDate = inquireDate;
	}
	public void setInquireDate(String inquireDate) {
		this.setInquireDate( str2Date(inquireDate) );
	}
	
	public Date getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}
	public void setVisitDate(String visitDate) {
		this.setVisitDate(str2Date(visitDate));
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

	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
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

	public Date getFirstAssignDate() {
		return firstAssignDate;
	}
	public void setFirstAssignDate(Date firstAssignDate) {
		this.firstAssignDate = firstAssignDate;
	}
	
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
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
	
	private Date str2Date(String s){
		java.util.Date d = null;
		try{
			if(StringUtils.isNotEmpty(s))
				d = DateUtils.parseDate(s, "yyyy-MM-dd");					
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return d;
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
	
	public String getBasicInfo() {
		return basicInfo;
	}
	public void setBasicInfo(String basicInfo) {
		this.basicInfo = basicInfo;
	}
	public String getLastInfo() {
		return lastInfo;
	}
	public void setLastInfo(String lastInfo) {
		this.lastInfo = lastInfo;
	}
	public String getCallCenter() {
		return callCenter;
	}
	public void setCallCenter(String callCenter) {
		this.callCenter = callCenter;
	}
	
	public String getStuCity() {
		return stuCity;
	}
	public void setStuCity(String stuCity) {
		this.stuCity = stuCity;
	}
	
	public String getToCity() {
		return toCity;
	}
	public void setToCity(String toCity) {
		this.toCity = toCity;
	}
	
	public Long getToCityId() {
		return toCityId;
	}
	public void setToCityId(Long toCityId) {
		this.toCityId = toCityId;
	}
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	
	public String getFlToefl() {
		return flToefl;
	}
	public void setFlToefl(String flToefl) {
		this.flToefl = flToefl;
	}
	public String getFlToeflJunior() {
		return flToeflJunior;
	}
	public void setFlToeflJunior(String flToeflJunior) {
		this.flToeflJunior = flToeflJunior;
	}
	public String getFlIelts() {
		return flIelts;
	}
	public void setFlIelts(String flIelts) {
		this.flIelts = flIelts;
	}
	public String getFlGre() {
		return flGre;
	}
	public void setFlGre(String flGre) {
		this.flGre = flGre;
	}
	public String getFlGmat() {
		return flGmat;
	}
	public void setFlGmat(String flGmat) {
		this.flGmat = flGmat;
	}
	public String getFlSat() {
		return flSat;
	}
	public void setFlSat(String flSat) {
		this.flSat = flSat;
	}
	public String getFlSat2() {
		return flSat2;
	}
	public void setFlSat2(String flSat2) {
		this.flSat2 = flSat2;
	}
	public String getFlSsat() {
		return flSsat;
	}
	public void setFlSsat(String flSsat) {
		this.flSsat = flSsat;
	}
	public String getFlAct() {
		return flAct;
	}
	public void setFlAct(String flAct) {
		this.flAct = flAct;
	}


	public Boolean getArchive() {
		return archive;
	}
	public void setArchive(Boolean archive) {
		this.archive = archive;
	}
	
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getZxgwNames() {
		return zxgwNames;
	}
	public void setZxgwNames(String zxgwNames) {
		this.zxgwNames = zxgwNames;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
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
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public Long getStuLevel() {
		return stuLevel;
	}
	public void setStuLevel(Long stuLevel) {
		this.stuLevel = stuLevel;
	}
	public String getStuLevelName() {
		return stuLevelName;
	}
	public void setStuLevelName(String stuLevelName) {
		this.stuLevelName = stuLevelName;
	}
}