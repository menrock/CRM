package com.niu.crm.model;

import java.util.Date;

/**
 * 合同归档记录
 * @author 天雨
 *
 */
public class CustContractArchive extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6354793024573610749L;

	private Long conId;
	private Date archiveDate;
	private Long creatorId;
	
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	
	public Date getArchiveDate() {
		return archiveDate;
	}
	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	
	
}
