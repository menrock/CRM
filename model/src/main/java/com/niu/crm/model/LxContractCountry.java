package com.niu.crm.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;


public class LxContractCountry extends BaseModel{
	private static final long serialVersionUID = -5254504846547494911L;
	
	private Long lxConId;
	private String countryCode;
	private String countryName;
	
	public Long getLxConId() {
		return lxConId;
	}
	public void setLxConId(Long lxConId) {
		this.lxConId = lxConId;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

}