package com.niu.crm.vo;

import com.niu.crm.model.CustAssign;

public class CpCustAssignVO extends CustAssign{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2947393563238419869L;
	
	private String zxgwName;
	private String creatorName;
	
	
	public String getZxgwName() {
		return zxgwName;
	}
	public void setZxgwName(String zxgwName) {
		this.zxgwName = zxgwName;
	}
	
	public String getCreatorName() {
		return this.creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
}
