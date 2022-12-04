package com.niu.crm.vo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.niu.crm.model.UserFunc;

public class UserFuncVO extends UserFunc{
	private static final long serialVersionUID = -7068426102488571562L;

	private String unitNames;
	private String companyNames;
	private String fromNames;
	private UserVO user;
	

	public UserVO getUser() {
		return user;
	}
	public void setUser(UserVO user) {
		this.user = user;
	}
	
	public String getUnitNames() {
		return unitNames;
	}
	public void setUnitNames(String unitNames) {
		this.unitNames = unitNames;
	}
	
	public String getCompanyNames() {
		return companyNames;
	}
	public void setCompanyNames(String companyNames) {
		this.companyNames = companyNames;
	}
	
	public String getFromNames() {
		return fromNames;
	}
	public void setFromNames(String fromNames) {
		this.fromNames = fromNames;
	}
	
	public Long[] getUnitIdList(){
		if(StringUtils.isEmpty(getUnitIds()))
			return null;
		
		String[] szIds = getUnitIds().split(",");
		Long[] ids = new Long[szIds.length];
		for(int i=0; i < szIds.length; i++){
			ids[i] = Long.parseLong(szIds[i]);
		}
		return ids;
	}
	
	public Long[] getCompanyIdList(){
		if(StringUtils.isEmpty(getCompanyIds()))
			return null;
		
		String[] szIds = getCompanyIds().split(",");
		Long[] ids = new Long[szIds.length];
		for(int i=0; i < szIds.length; i++){
			ids[i] = Long.parseLong(szIds[i]);
		}
		return ids;
	}

	public Long[] getFromIdList(){
		if(StringUtils.isEmpty(getFromIds()))
			return null;
		
		String[] szIds = getFromIds().split(",");
		Long[] ids = new Long[szIds.length];
		for(int i=0; i < szIds.length; i++){
			ids[i] = Long.parseLong(szIds[i]);
		}
		return ids;
	}
}
