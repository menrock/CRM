package com.niu.crm.form;

import java.util.Date;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.niu.crm.util.DateUtil;

public class StuShareSearchForm {
	private Long fromUserId;
	private Long toUserId;
	private Long stuId;
	
	private Long creatorId;
	private Date created_from;
	private Date created_to;
	
	public Date getCreated_to() {
		return created_to;
	}
	public void setCreated_to(String created_to) throws ParseException{
		this.created_to = DateUtil.parseDate(created_to);
	}
	
	public Long getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}
	
	public Long getToUserId() {
		return toUserId;
	}
	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}
	
	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
		
	public Date getCreated_from() {
		return created_from;
	}
	public void setCreated_from(String created_from) throws ParseException{
		this.created_from = DateUtil.parseDate(created_from);
	}
}
