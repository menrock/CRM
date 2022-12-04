package com.niu.crm.vo;

import java.util.List;

import com.niu.crm.model.ZxgwTeam;

public class ZxgwTeamVO extends ZxgwTeam {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5054897637111877814L;
	
	private String zxgwName;
	private String leaderName;

	private List<Long> memberIds;
	private String memeberNames;
	private List<ZxgwTeam> members;

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

	public String getMemeberNames() {
		return memeberNames;
	}
	public void setMemeberNames(String memeberNames) {
		this.memeberNames = memeberNames;
	}
	public List<ZxgwTeam> getMembers() {
		return members;
	}
	public void setMembers(List<ZxgwTeam> members) {
		this.members = members;
	}
	public List<Long> getMemberIds() {
		return memberIds;
	}
	public void setMemberIds(List<Long> memberIds) {
		this.memberIds = memberIds;
	}

}
