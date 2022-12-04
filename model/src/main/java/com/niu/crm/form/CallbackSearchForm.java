package com.niu.crm.form;

import java.util.Date;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.niu.crm.model.type.CallbackRemindType;
import com.niu.crm.util.DateUtil;

public class CallbackSearchForm {
	
	//回访类型
	private String callbackType;
	
	private Long company_id;
	private Long unitId;
	private String unitCode;
	
	private Long source_zxgw_id;
	private Long zxgw_id;
	private Long owner_id;
	private Long creator_id;
	private String stuFromCode;
	private Long[] stuFromIds;

	private Boolean signFlag;
	private Long[] stu_level; 
	private String visitor_type; 
	private String[] country_codes;
	private String[] plan_xl;
	private Date assign_from;
	private Date assign_to;
	private Date latest_contact_from;
	private Date latest_contact_to;
	
	//提醒时间
	private Date created_from;
	private Date created_to;
	//回访状态(none,ontime, delayed, finish)
	private String callback_status;
	
	private String aclClause;
	
	
	public Long getCompany_id() {
		return company_id;
	}
	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	
	public Long getSource_zxgw_id() {
		return source_zxgw_id;
	}
	public void setSource_zxgw_id(Long source_zxgw_id) {
		this.source_zxgw_id = source_zxgw_id;
	}
	
	public Long getZxgw_id() {
		return zxgw_id;
	}
	public void setZxgw_id(Long zxgw_id) {
		this.zxgw_id = zxgw_id;
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
	
	public Long[] getStu_level() {
		return stu_level;
	}
	public void setStu_level(Long[] stu_level) {
		if(stu_level==null || stu_level.length==0)
			this.stu_level = null;
		else
			this.stu_level = stu_level;
	}
	
	public String getCallbackType() {
		return callbackType;
	}
	public void setCallbackType(CallbackRemindType callbackType) {
		if(callbackType == null)
			this.callbackType = null;
		else
			this.callbackType = callbackType.toString();
	}
	
	public Date getLatest_contact_from() {
		return latest_contact_from;
	}
	public void setLatest_contact_from(String latest_contact_from) throws ParseException{
		this.latest_contact_from = DateUtil.parseDate(latest_contact_from);
	}
	public Date getLatest_contact_to() {
		return latest_contact_to;
	}
	public void setLatest_contact_to(String latest_contact_to) throws ParseException{
		this.latest_contact_to = DateUtil.parseDate(latest_contact_to);
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
	
	public String getAclClause(){
		return this.aclClause;
	}
	public void setAclClause(String aclClause){
		this.aclClause = aclClause;
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
	public String getCallback_status() {
		return callback_status;
	}
	public void setCallback_status(String callback_status) {
		if(StringUtils.isEmpty(callback_status))
			this.callback_status = null;
		else
			this.callback_status = callback_status.toLowerCase();
	}
}
