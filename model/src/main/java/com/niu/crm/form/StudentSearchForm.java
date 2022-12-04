package com.niu.crm.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.niu.crm.util.DateUtil;

public class StudentSearchForm {	
	private Boolean fuzzySearch; //模糊查询
	
	private Long company_id;
	private String unitCode;
	private List<Long> zxgwList;
	private Boolean assignFlag;
	private Long owner_id;
	private Long creator_id;
	private String name;
	private String gender; 
	private String mobile; 
	private String phone; 
	private String stuFromCode;
	private Long[] stuFromIds;

	private Boolean signFlag;
	private String curr_school; 
	private String school_input_flag;
	private String[] stu_level; 
	private String[] exclude_stu_level; 
	private String visitor_type; 
	private String[] country_codes;
	private String[] plan_xl;
	private Date inquire_from;
	private Date inquire_to;
	private Date visit_from;
	private Date visit_to;
	private Date assign_from;
	private Date assign_to;
	private Date last_contact_from;
	private Date last_contact_to;
	private Integer contactCountMin;
	private Integer contactCountMax;
	private String exclude_ids;
	
	private Date created_from;
	private Date created_to;
	
	private Integer planEnterYear;
	private String planEnterSeason;
	
	private String aclClause;
	
	public StudentSearchForm(){
		fuzzySearch = Boolean.FALSE;
	}
	
	public Long getCompany_id() {
		return company_id;
	}
	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		if(StringUtils.isEmpty(unitCode))
			this.unitCode = null;
		else
			this.unitCode = unitCode;
	}
	
	public List<Long> getZxgwList() {
		return zxgwList;
	}
	public void setZxgwList(List<Long> zxgwList) {
		this.zxgwList = zxgwList;
	}
	public void addZxgw(Long zxgwId){
		if(zxgwId == null)
			return;
		if(zxgwList == null)
			zxgwList = new ArrayList<Long>();
		zxgwList.add(zxgwId);
	}
	public void setZxgw_id(Long zxgwId){
		this.addZxgw(zxgwId);
	}
	
	public Boolean getAssignFlag() {
		return assignFlag;
	}
	public void setAssignFlag(Boolean assignFlag) {
		this.assignFlag = assignFlag;
	}
	
	public Long getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(Long ownerId) {
		this.owner_id = ownerId;
	}
	
	public Long getCreator_id() {
		return creator_id;
	}
	public void setCreator_id(Long creator_id) {
		this.creator_id = creator_id;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		if(StringUtils.isEmpty(name))
			this.name = null;
		else if( fuzzySearch )
			this.name = "%" + name.trim() + "%";
		else
			this.name = name.trim();
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		if(StringUtils.isEmpty(mobile) )
			this.mobile = null;
		else
			this.mobile = mobile.trim();
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getVisitor_type() {
		return visitor_type;
	}
	public void setVisitor_type(String visitorType) {
		if(StringUtils.isEmpty(visitorType))
			this.visitor_type = null;
		else
			this.visitor_type = visitorType;
	}
	
	public String[] getCountry_codes() {
		if(country_codes == null || country_codes.length ==0)
			return null;
		
		return country_codes;
	}
	public void setCountry_codes(String[] country_codes) {
		this.country_codes = country_codes;
	}
	
	public String[] getPlan_xl() {
		return plan_xl;
	}
	public void setPlan_xl(String[] plan_xl) {
		this.plan_xl = plan_xl;
	}
		
	public Date getCreated_from() {
		return created_from;
	}
	public void setCreated_from(String created_from) throws ParseException{
		this.created_from = DateUtil.parseDate(created_from);
	}
	
	public Date getCreated_to() {
		return created_to;
	}
	public void setCreated_to(String created_to) throws ParseException{
		this.created_to = DateUtil.parseDate(created_to);
	}
	public Date getInquire_from() {
		return inquire_from;
	}
	public void setInquire_from(String inquire_from) throws ParseException{
		this.inquire_from = DateUtil.parseDate(inquire_from);
	}
	public Date getInquire_to() {
		return inquire_to;
	}
	public void setInquire_to(String inquire_to) throws ParseException{
		this.inquire_to = DateUtil.parseDate(inquire_to);
	}
	public Date getVisit_from() {
		return visit_from;
	}	
	public void setVisit_from(String visit_from) throws ParseException{
		this.visit_from = DateUtil.parseDate(visit_from);
	}
	
	public Date getVisit_to() {
		return visit_to;
	}
	public void setVisit_to(String visit_to) throws ParseException{
		this.visit_to = DateUtil.parseDate(visit_to);
	}

	public Date getAssign_from() {
		return assign_from;
	}	
	public void setAssign_from(String assign_from) throws ParseException{
		this.assign_from = DateUtil.parseDate(assign_from);
	}
	
	public Date getAssign_to() {
		return assign_to;
	}
	public void setAssign_to(String assign_to) throws ParseException{
		this.assign_to = DateUtil.parseDate(assign_to);
	}
	
	public String getCurr_school() {
		if(StringUtils.isEmpty(curr_school))
			return null;
		
		if( fuzzySearch )
			return "%" + curr_school + "%";
		else
			return curr_school;
	}
	public void setCurr_school(String curr_school) {
		this.curr_school = curr_school;
	}
	
	public String[] getStu_level() {
		return stu_level;
	}
	public void setStu_level(String[] stu_level) {
		if(stu_level==null || stu_level.length==0)
			this.stu_level = null;
		else
			this.stu_level = stu_level;
	}

	public String[] getExclude_stu_level() {
		return exclude_stu_level;
	}
	public void setExclude_stu_level(String[] exclude_stu_level) {
		if(exclude_stu_level==null || exclude_stu_level.length==0)
			this.exclude_stu_level = null;
		else
			this.exclude_stu_level = exclude_stu_level;
	}
	
	public String getSchool_input_flag() {
		return school_input_flag;
	}
	public void setSchool_input_flag(String school_input_flag) {
		if(StringUtils.isEmpty(school_input_flag))
			this.school_input_flag = null;
		else
			this.school_input_flag = school_input_flag;
	}
	
	public String getExclude_ids() {
		return exclude_ids;
	}
	public void setExclude_ids(String exclude_ids) {
		if(StringUtils.isEmpty(exclude_ids)){
			this.exclude_ids = null;
		}else{
			this.exclude_ids = exclude_ids.replaceAll("'", "");
		}
	}
	public Date getLast_contact_from() {
		return last_contact_from;
	}
	public void setLast_contact_from(String last_contact_from) throws ParseException{
		this.last_contact_from = DateUtil.parseDate(last_contact_from);
	}
	public Date getLast_contact_to() {
		return last_contact_to;
	}
	public void setLast_contact_to(String last_contact_to) throws ParseException{
		this.last_contact_to = DateUtil.parseDate(last_contact_to);
	}

	public Integer getContactCountMin() {
		return contactCountMin;
	}
	public void setContactCountMin(Integer contactCountMin) {
		this.contactCountMin = contactCountMin;
	}

	public Integer getContactCountMax() {
		return contactCountMax;
	}
	public void setContactCountMax(Integer contactCountMax) {
		this.contactCountMax = contactCountMax;
	}
	
	public String getAclClause(){
		return this.aclClause;
	}
	public void setAclClause(String aclClause){
		this.aclClause = aclClause;
	}

	public Boolean getFuzzySearch() {
		return fuzzySearch;
	}

	public void setFuzzySearch(Boolean fuzzySearch) {
		this.fuzzySearch = fuzzySearch;
	}

	public String getStuFromCode() {
		return stuFromCode;
	}

	public void setStuFromCode(String stuFromCode) {
		if(StringUtils.isEmpty(stuFromCode))
			this.stuFromCode = null;
		else
			this.stuFromCode = stuFromCode;
	}

	public Long[] getStuFromIds() {
		return stuFromIds;
	}
	public void setStuFromIds(Long[] stuFromIds) {
		if(stuFromIds == null || stuFromIds.length ==0)
			this.stuFromIds = null;
		else
			this.stuFromIds = stuFromIds;
	}

	public Boolean getSignFlag() {
		return signFlag;
	}

	public void setSignFlag(Boolean signFlag) {
		this.signFlag = signFlag;
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
		if(StringUtils.isBlank(planEnterSeason))
			this.planEnterSeason = null;
		else
			this.planEnterSeason = planEnterSeason;
	}
}
