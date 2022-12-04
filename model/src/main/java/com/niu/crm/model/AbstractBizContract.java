package com.niu.crm.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.niu.crm.model.type.LxContractStatus;

/** 
 * @author seker(seker@ixiaopu.com)
 * @date 2017年3月29日
 */
public abstract class AbstractBizContract extends BaseModel {
	private static final long serialVersionUID = 4500995249405363289L;
	
	private Long cstmId; 
	private String cstmName;
	private Long conId;
	private String conNo;
	private BigDecimal conValue;
	private BigDecimal conDiscount;
	private BigDecimal skValue;
	private Date firstSkDate;
	private Long companyId;
	private String companyName;
	private String memo;
	private Date signDate;
	private Date archiveDate;
	private Long creatorId;
	
	private List<CustContractLine> feeLines = null;

	public Long getCstmId() {
		return cstmId;
	}
	public void setCstmId(Long cstmId) {
		this.cstmId = cstmId;
	}
	
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	
	public String getConNo() {
		return conNo;
	}
	public void setConNo(String conNo) {
		this.conNo = conNo;
	}
	
	public List<CustContractLine> getFeeLines() {
		return feeLines;
	}
	public void setFeeLines(List<CustContractLine> feeLines) {
		this.feeLines = feeLines;
	}
	
	public BigDecimal getConValue() {
		return conValue;
	}
	public void setConValue(BigDecimal conValue) {
		this.conValue = conValue;
	}
	
	public BigDecimal getConDiscount() {
		return conDiscount;
	}
	public void setConDiscount(BigDecimal conDiscount) {
		this.conDiscount = conDiscount;
	}
	
	public BigDecimal getSkValue() {
		return skValue;
	}
	public void setSkValue(BigDecimal skValue) {
		this.skValue = skValue;
	}
	
	public Date getFirstSkDate() {
		return firstSkDate;
	}
	public void setFirstSkDate(Date firstSkDate) {
		this.firstSkDate = firstSkDate;
	}
	
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
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
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
	public String getCstmName() {
		return cstmName;
	}
	public void setCstmName(String cstmName) {
		this.cstmName = cstmName;
	}
}
