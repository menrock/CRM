package com.niu.crm.form;

import java.util.Date;

import com.niu.crm.model.type.CallbackType;

public class CallbackCheckForm {
	private Long companyId;
	private CallbackType callbackType;
	
	private Date dateFrom;
	private Date dateTo;
	
	private Long zxgwId;
	private Long stuFromId;
	private String stuFromCode;

	private Date stuCreatedFrom;
	private Date stuCreatedTo;
	
	//ONTIME/FINISH/NOFINISH/OVERTIME_FINISH(按时完成， 已回访，未回访， 逾期回访)
	private String callbackStatus;
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	public CallbackType getCallbackType() {
		return callbackType;
	}
	public void setCallbackType(CallbackType callbackType) {
		this.callbackType = callbackType;
	}
	
	public Date getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Date getDateTo() {
		return dateTo;
	}
	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
	
	
	public String getCallbackStatus() {
		return callbackStatus;
	}
	public void setCallbackStatus(CallbackStatus callbackStatus) {
		if(callbackStatus == null)
			this.callbackStatus = null;
		else
			this.callbackStatus = callbackStatus.toString();
	}
	
	public Date getStuCreatedFrom() {
		return stuCreatedFrom;
	}
	public void setStuCreatedFrom(Date stuCreatedFrom) {
		this.stuCreatedFrom = stuCreatedFrom;
	}

	public Date getStuCreatedTo() {
		return stuCreatedTo;
	}
	public void setStuCreatedTo(Date stuCreatedTo) {
		this.stuCreatedTo = stuCreatedTo;
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

	public Long getZxgwId() {
		return zxgwId;
	}
	public void setZxgwId(Long zxgwId) {
		this.zxgwId = zxgwId;
	}

	public static enum CallbackStatus {
		ONTIME("按时完成"), FINISH("完成"), NOFINISH("没完成"),
		OVERTIME_FINISH("逾期完成");
		
		private String name;
		
		CallbackStatus(String name){
			this.name = name;
		}
		
		public String getName(){
			return this.name;
		}
	}
}
