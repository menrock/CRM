package com.niu.crm.vo;

import com.niu.crm.model.Unit;
import com.niu.crm.model.User;


public class UserVO extends User{
	/**
	 * 
	 */
	private static final long serialVersionUID = 139193044316906433L;
	
	//0不存在 1=已关注，2=已禁用，4=未关注
	private Integer wxStatus;
	private String companyName;
	private Unit unit;


	public Unit getUnit() {
		return unit;
	}


	public void setUnit(Unit unit) {
		this.unit = unit;
	}


	public String getCompanyName() {
		return companyName;
	}


	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public Integer getWxStatus() {
		return wxStatus;
	}

	public void setWxStatus(Integer wxStatus) {
		this.wxStatus = wxStatus;
	}
}
