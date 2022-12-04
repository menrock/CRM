package com.niu.crm.vo;

import com.niu.crm.model.LxCustAssign;

public class LxCustAssignVO extends LxCustAssign{
	private static final long serialVersionUID = 3850639031802574311L;
	
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
