package com.niu.crm.form;


public class SysMessageSearchForm {
	private Long ownerId;
	private Long senderId;
	private Integer status;
	private String statusOp; //( >, =, <)
	
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
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getStatusOp() {
		return statusOp;
	}
	public void setStatusOp(String statusOp) {
		this.statusOp = statusOp;
	}
}
