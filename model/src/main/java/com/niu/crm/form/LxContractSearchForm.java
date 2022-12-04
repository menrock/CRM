package com.niu.crm.form;

import java.util.Date;
import java.text.ParseException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.CollectionUtils;

import com.niu.crm.util.DateUtil;

public class LxContractSearchForm {	
	private Boolean fuzzySearch; //模糊查询
	private Long companyId;
	private String[] conTypeCodes;
	private String conNo;
	private String stuFromCode;
	
	private Long signGwId;
	private Long planGwId;
	private Long applyGwId;
	private Long writeGwId;
	private Long serviceGwId;
	private Long creatorId;
	private Long stuId;
	private Long cstmId;
	private String cstmName;
	private String mobile; 
	private String phone; 

	private String status; 
	
	private Date signFrom;
	private Date signTo;

	private Date archiveFrom;
	private Date archiveTo;
	
	private Date createdFrom;
	private Date createdTo;
	private Integer tranFlag;
	
	//用于查询后期顾问 归属case
	private Long caseGwId;
	
	private String aclClause;
	
	public LxContractSearchForm(){
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
	
	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
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
	
	public String[] getConTypeCodes() {
		return conTypeCodes;
	}
	public void setConTypeCodes(String[] conTypeCodes) {
		if(ArrayUtils.isEmpty(conTypeCodes) )
			this.conTypeCodes = null;
		else
			this.conTypeCodes = conTypeCodes;
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

	public Date getArchiveFrom() {
		return archiveFrom;
	}
	public void setArchiveFrom(Date archiveFrom) {
		this.archiveFrom = archiveFrom;
	}

	public Date getArchiveTo() {
		return archiveTo;
	}
	public void setArchiveTo(Date archiveTo) {
		this.archiveTo = archiveTo;
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

	public Integer getTranFlag() {
		return tranFlag;
	}
	public void setTranFlag(Integer tranFlag) {
		this.tranFlag = tranFlag;
	}

	public Long getCaseGwId() {
		return caseGwId;
	}

	public void setCaseGwId(Long caseGwId) {
		this.caseGwId = caseGwId;
	}
}
