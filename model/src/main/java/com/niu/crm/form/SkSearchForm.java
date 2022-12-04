package com.niu.crm.form;

import java.util.Date;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.niu.crm.util.DateUtil;

public class SkSearchForm {	
	private Boolean fuzzySearch; //模糊查询
	private Long companyId;

	private String[] conTypeCodes;
	private String conNo;
	private Long stuFromId;
	private String stuFromCode;
	
	private Long signGwId;
	private Long planGwId;
	private Long applyGwId;
	private Long writeGwId;
	private Long serviceGwId;
	private Long creatorId;
	private Long conId;
	private Long itemId;
	private String itemCode;
	private Long cstmId;
	private String cstmName;
	private String mobile; 
	private String phone; 

	private String status; 
	
	private Date skDateFrom;
	private Date skDateTo;
	
	private Date createdFrom;
	private Date createdTo;
	
	private String aclClause;
	
	public SkSearchForm(){
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
	
	public Long getPlanGwId() {
		return planGwId;
	}
	public void setPlanGwId(Long planGwId) {
		this.planGwId = planGwId;
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
	
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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
		if(StringUtils.isEmpty(phone) )
			this.phone = null;
		else
			this.phone = phone;
	}
	
	public String[] getConTypeCodes() {
		return conTypeCodes;
	}
	public void setConTypeCodes(String[] conTypeCodes) {
		if( conTypeCodes == null || conTypeCodes.length == 0)
			this.conTypeCodes = null;
		else
			this.conTypeCodes = conTypeCodes;
	}
	
	public String getConNo() {
		return conNo;
	}
	public void setConNo(String conNo) {
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

	public Date getSkDateFrom() {
		return skDateFrom;
	}	
	public void setSkDateFrom(String skDateFrom) throws ParseException{
		this.skDateFrom = DateUtil.parseDate(skDateFrom);
	}
	
	public Date getSkDateTo() {
		return skDateTo;
	}
	public void setSkDateTo(String szSkDateTo) throws ParseException{
		if(StringUtils.isEmpty(szSkDateTo)){
			this.skDateTo = null;
		}else{
			if(szSkDateTo.length() ==10)
				szSkDateTo += " 23:59:59";
			this.skDateTo = DateUtil.parseDate(szSkDateTo);
		}
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

	public Long getStuFromId() {
		return stuFromId;
	}
	public void setStuFromId(Long stuFromId) {
		this.stuFromId = stuFromId;
	}

	public String getStuFromCode() {
		return stuFromCode;
	}
	public void setStuFromCode(String stuFromCode) {
		this.stuFromCode = stuFromCode;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
