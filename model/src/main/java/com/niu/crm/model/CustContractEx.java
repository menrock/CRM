package com.niu.crm.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.niu.crm.model.CustContract;
import com.niu.crm.model.CustContractLine;
import com.niu.crm.model.CustContractSk;

public class CustContractEx extends CustContract{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8165672970874779033L;
	
	private Long cstmId;
	private String cstmName;
	private String fromCode;
	
	private List<CustContractSk> skLines = null;
	private List<CustContractLine> feeLines = null;
	
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
	
	public String getFromCode() {
		return fromCode;
	}
	public void setFromCode(String fromCode) {
		this.fromCode = fromCode;
	}
	public List<CustContractSk> getSkLines() {
		return skLines;
	}
	public void setSkLines(List<CustContractSk> skLines) {
		this.skLines = skLines;
	}
	public List<CustContractLine> getFeeLines() {
		return feeLines;
	}
	public void setFeeLines(List<CustContractLine> feeLines) {
		this.feeLines = feeLines;
	}
	
}
