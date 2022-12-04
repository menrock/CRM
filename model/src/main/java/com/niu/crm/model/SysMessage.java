package com.niu.crm.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

public class SysMessage extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7615938418114060712L;
	
	private Long stuId;
	//链接地址
	private String url;
	private String content;
	private Integer status;
	private Long ownerId;
	private Long senderId;
	private String senderName;
	private String sysMemo;

	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Long getSenderId() {
		return senderId;
	}
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	public String getSysMemo() {
		return sysMemo;
	}
	public void setSysMemo(String sysMemo) {
		this.sysMemo = sysMemo;
	}
}