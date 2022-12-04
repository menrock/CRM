package com.niu.crm.form;

import java.util.Date;
import java.text.ParseException;

import com.niu.crm.util.DateUtil;

public class CustAssignSearchForm {
	private Long stuId;
	private Long zxgwId;
	
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
		
	public Date getCreatedFrom() {
		return createdFrom;
	}
	public void setCreatedFrom(Date createdFrom) throws ParseException{
		this.createdFrom = createdFrom;
	}
	public void setCreatedFrom(String createdFrom) throws ParseException{
		this.createdFrom = DateUtil.parseDate(createdFrom);
	}
	
	public Date getCreatedTo() {
		return createdTo;
	}
	public void setCreatedTo(Date createdTo) throws ParseException{
		this.createdTo = createdTo;
	}
	public void setCreatedTo(String createdTo) throws ParseException{
		this.createdTo = DateUtil.parseDate(createdTo);
	}
}
