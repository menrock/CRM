package com.niu.crm.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Maxid extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2346431100201745136L;
	
	private String idCode;
	private Integer day;
	private Long maxid;

	public String getIdCode() {
		return idCode;
	}
	public void setIdCode(String idCode) {
		this.idCode = idCode;
	} 
	
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	
	public Long getMaxid() {
		return maxid;
	}
	public void setMaxid(Long maxid) {
		this.maxid = maxid;
	}
}