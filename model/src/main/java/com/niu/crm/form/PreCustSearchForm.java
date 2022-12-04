package com.niu.crm.form;

import java.util.Date;
import java.util.List;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;

import com.niu.crm.model.type.PreCustStatus;
import com.niu.crm.util.DateUtil;

public class PreCustSearchForm {	
	private Boolean fuzzySearch; //模糊查询
	
	private Long company_id;
	private Long creator_id;
	private String name;
	private String gender; 
	private String mobile; 
	private String phone; 
	private String stuFromCode;
	private Long[] stuFromIds;

	private String curr_school; 
	private List<PreCustStatus> statusList; 
	private String[] country_codes;
	private String[] plan_xl;
	private Date last_contact_from;
	private Date last_contact_to;
	private Integer contactCountMin;
	private Integer contactCountMax;
	private String exclude_cstm_ids;
	
	private Date created_from;
	private Date created_to;
	
	private Integer planEnterYear;
	private String planEnterSeason;
	
	private String aclClause;
	
	public PreCustSearchForm(){
		fuzzySearch = Boolean.FALSE;
	}
	
	public Long getCompany_id() {
		return company_id;
	}
	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
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
	
	public List<PreCustStatus> getStatusList() {
		return statusList;
	}
	public void setStatusList(List<PreCustStatus> statusList) {
		if(statusList==null || statusList.size()==0)
			this.statusList = null;
		else
			this.statusList = statusList;
	}

	
	public String getExclude_cstm_ids() {
		return exclude_cstm_ids;
	}
	public void setExclude_cstm_ids(String exclude_cstm_ids) {
		if(StringUtils.isEmpty(exclude_cstm_ids)){
			this.exclude_cstm_ids = null;
		}else{
			this.exclude_cstm_ids = exclude_cstm_ids.replaceAll("'", "");
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
