package com.niu.crm.form;

import java.util.Date;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.niu.crm.model.type.CallbackRemindType;
import com.niu.crm.util.DateUtil;

public class CallbackSimpleSearchForm {
	
	//回访类型
	private CallbackRemindType callbackType;

	private Long stuId;
	private Long zxgwId;
	private Long source_zxgw_id;

	private Date latest_contact_from;
	private Date latest_contact_to;
	
	//提醒时间
	private Date createdFrom;
	private Date createdTo;

	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	
	public Long getZxgwId() {
		return zxgwId;
	}
	public void setZxgwId(Long zxgwId) {
		this.zxgwId = zxgwId;
	}
	
	public Long getSource_zxgw_id() {
		return source_zxgw_id;
	}
	public void setSource_zxgw_id(Long source_zxgw_id) {
		this.source_zxgw_id = source_zxgw_id;
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
	public void setCreatedTo(String createdTo) throws ParseException{
		this.createdTo = DateUtil.parseDate(createdTo);
	}
	
	public CallbackRemindType getCallbackType() {
		return callbackType;
	}
	public void setCallbackType(CallbackRemindType callbackType) {
		this.callbackType = callbackType;
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
}
