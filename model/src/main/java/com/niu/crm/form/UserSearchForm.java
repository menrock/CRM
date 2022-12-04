package com.niu.crm.form;

import java.util.Date;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public class UserSearchForm {
	private Long companyId;
	private Long unitId;
	private String unitCode;
	private String name;
	private String account;
	private String gender; 
	private String phone;
	private Boolean online;
	private Boolean enabled;
	private Date created_from;
	private Date created_to;
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		if(StringUtils.isEmpty(account))
			this.account = null;
		else
			this.account = account.trim().toLowerCase();
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public Boolean getOnline() {
		return online;
	}
	public void setOnline(Boolean online) {
		this.online = online;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
		
	public Date getCreated_from() {
		return created_from;
	}
	public void setCreated_from(String created_from) throws ParseException{
		java.util.Date d = null;
		if(StringUtils.isNotEmpty(created_from))
			d = DateUtils.parseDate(created_from, "yyyy-MM-dd");			
		
		this.created_from = d;
	}
	
	public Date getCreated_to() {
		return created_to;
	}
	public void setCreated_to(String created_to) throws ParseException{
		this.created_to = str2Date(created_to);
	}
	
	private Date str2Date(String s){
		java.util.Date d = null;
		try{
			if(StringUtils.isNotEmpty(s))
				d = DateUtils.parseDate(s, "yyyy-MM-dd");					
		}catch(Exception e){
			e.printStackTrace();
		}		
		
		return d;
	}
}
