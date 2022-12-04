package com.niu.crm.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class CustContract extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1530445063535167894L;
	
	private Long cstmId;
	private String cstmName;
	private String conNo;
	private Date signDate;
	
	
	private Long creatorId;
	
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
	
	public String getConNo() {
		return conNo;
	}
	public void setConNo(String conNo) {
		if(StringUtils.isEmpty(conNo))
			this.conNo = null;
		else
			this.conNo = conNo;
	}

	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
}
