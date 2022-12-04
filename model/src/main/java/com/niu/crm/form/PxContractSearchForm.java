package com.niu.crm.form;

import java.util.Date;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.niu.crm.util.DateUtil;

public class PxContractSearchForm {	
	private Boolean fuzzySearch; //模糊查询
	private Long companyId;
	private String conNo;
	private String stuFromCode;
	
	private Long signGwId;
	private Long gwId;
	private Long cstmId;
	private String cstmName;
	private String mobile; 
	private String phone; 

	private String status; 
	
	private Date signFrom;
	private Date signTo;
	
	private Date createdFrom;
	private Date createdTo;
	
	private Long creatorId;
	
	private String aclClause;
	
	public PxContractSearchForm(){
		fuzzySearch = Boolean.FALSE;
	}
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	public Long getSignGwId() {
		return signGwId;
	}
	public void setSignGwId(Long signGwId) {
		this.signGwId = signGwId;
	}
	
	public Long getGwId() {
		return gwId;
	}
	public void setGwId(Long gwId) {
		this.gwId = gwId;
	}
	
	public Long getCstmId() {
		return cstmId;
	}
	public void setCstmId(Long cstmId) {
		this.cstmId = cstmId;
	}

	public String getCstmName() {
		return this.cstmName;
	}
	public void setCstmName(String cstmName) {
		if(StringUtils.isEmpty(cstmName))
			this.cstmName = null;
		else if( fuzzySearch )
			this.cstmName = "%" + cstmName.trim() + "%";
		else
			this.cstmName = cstmName.trim();
	}
	
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creator_id) {
		this.creatorId = creator_id;
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
	
	public String getConNo() {
		return conNo;
	}
	public void setConNo(String conNo) {
		if(StringUtils.isEmpty(conNo))
			this.conNo = null;
		else
			this.conNo = conNo;
	}
		
	public Date getCreatedFrom() {
		return createdFrom;
	}
	public void setCreatedFrom(String createdFrom) throws ParseException{
		this.createdFrom = DateUtil.parseDate(createdFrom);
	}
	
	public Date getCreatedTo() {
		return createdTo;
	}
	public void setCreatedTo(String created_to) throws ParseException{
		this.createdTo = DateUtil.parseDate(created_to);
	}

	public Date getSignFrom() {
		return signFrom;
	}	
	public void setSignFrom(String szSignFrom) throws ParseException{
		this.signFrom = DateUtil.parseDate(szSignFrom);
	}
	
	public Date getSignTo() {
		return signTo;
	}
	public void setSignTo(String szSignTo) throws ParseException{
		this.signTo = DateUtil.parseDate(szSignTo);
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

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
