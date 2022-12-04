package com.niu.crm.vo;

import com.niu.crm.model.LxVisitInvite;

public class LxVisitInviteVO extends LxVisitInvite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7885436195678319822L;
	
	private Long companyId;
	private String companyName;
	
	private String gwName;
	private Long cstmId;
	private String cstmName;
	private String creatorName;
	private String visitCreatorName;
	

	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getGwName(){
		return this.gwName;
	}
	public void setGwName(String gwName){
		this.gwName = gwName;
	}
	
	public Long getCstmId() {
		return cstmId;
	}
	public void setCstmId(Long cstmId) {
		this.cstmId = cstmId;
	}
	
	public String getCstmName() {
		return cstmName;
	}
	public void setCstmName(String cstmName) {
		this.cstmName = cstmName;
	}
	
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	
	public String getVisitCreatorName() {
		return visitCreatorName;
	}
	public void setVisitCreatorName(String visitCreatorName) {
		this.visitCreatorName = visitCreatorName;
	}
}
