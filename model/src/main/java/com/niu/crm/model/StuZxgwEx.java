package com.niu.crm.model;

public class StuZxgwEx extends StuZxgw{
	private static final long serialVersionUID = -7263320029748540968L;
	

	private String zxgwName;
	private String stuLevelName;
	
	private Long cstmId;
	private String cstmName;
	private Long stuFromId;

	public String getZxgwName() {
		return zxgwName;
	}
	public void setZxgwName(String zxgwName) {
		this.zxgwName = zxgwName;
	}
	public String getStuLevelName() {
		return stuLevelName;
	}
	public void setStuLevelName(String stuLevelName) {
		this.stuLevelName = stuLevelName;
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
	public Long getStuFromId() {
		return stuFromId;
	}
	public void setStuFromId(Long stuFromId) {
		this.stuFromId = stuFromId;
	}
	
}