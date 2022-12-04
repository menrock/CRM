package com.niu.crm.model;

import java.util.Date;

public class CustAssign extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5868671887007373376L;
	public static final String OP_ASSIGN="assign";
	public static final String OP_REVOKE="revoke";
	
	
	private Long stuId;
	private Long zxgwId;
	private String opType;
	private Long creatorId;
	
	public CustAssign(){
		super.setCreatedAt(new Date(System.currentTimeMillis()));
	}
		
	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	
	public Long getZxgwId() {
		return zxgwId;
	}
	public void setZxgwId(Long zxgwId) {
		this.zxgwId = zxgwId;
	}

	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	
}
