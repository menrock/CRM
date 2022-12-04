package com.niu.crm.model;


public class ZxgwTeam extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6752953700625156167L;
	

	private Long zxgwId;
	private Long leaderId;
	
	private String zxgwName;
	private String leaderName;
	
	
	private Long creatorId;

	public Long getZxgwId() {
		return zxgwId;
	}

	public void setZxgwId(Long zxgwId) {
		this.zxgwId = zxgwId;
	}

	public Long getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(Long leaderId) {
		this.leaderId = leaderId;
	}

	public String getZxgwName() {
		return zxgwName;
	}

	public void setZxgwName(String zxgwName) {
		this.zxgwName = zxgwName;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
}
