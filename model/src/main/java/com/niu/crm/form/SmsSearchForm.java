package com.niu.crm.form;

import java.util.Date;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.niu.crm.util.DateUtil;

public class SmsSearchForm {
	private Long creatorId;
	private Long stuId;
	private String mobile;
	
	private Date createdFrom;
	private Date createdTo;
	
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
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
		
	public Date getCreatedFrom() {
		return createdFrom;
	}
	public void setCreatedFrom(String created_from) throws ParseException{
		this.createdFrom = DateUtil.parseDate(created_from);
	}
	
	public Date getCreatedTo() {
		return createdTo;
	}
	public void setCreatedTo(String createdTo) throws ParseException{
		this.createdTo = DateUtil.parseDate(createdTo);
	}
}
