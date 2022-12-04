package com.niu.crm.vo;

import com.niu.crm.model.LxContractTranApply;

public class LxContractTranApplyVO extends LxContractTranApply {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7559781923836504743L;
	
	private Long companyId;
	private String companyName;
	private Long stuId;
	private Long cstmId;
	private String cstmName;
	private String conNo;
	
	private String fromZxgwName;
	private String toZxgwName;
	private String fromPlanGwName;
	private String toPlanGwName;
	private String fromApplyGwName;
	private String toApplyGwName;
	private String fromWriteGwName;
	private String toWriteGwName;
	private String fromServiceGwName;
	private String toServiceGwName;
	
	private String creatorName;



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
	
	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
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

	public String getConNo() {
		return conNo;
	}
	public void setConNo(String conNo) {
		this.conNo = conNo;
	}

	public String getFromZxgwName() {
		return fromZxgwName;
	}

	public void setFromZxgwName(String fromZxgwName) {
		this.fromZxgwName = fromZxgwName;
	}

	public String getToZxgwName() {
		return toZxgwName;
	}

	public void setToZxgwName(String toZxgwName) {
		this.toZxgwName = toZxgwName;
	}

	public String getFromPlanGwName() {
		return fromPlanGwName;
	}

	public void setFromPlanGwName(String fromPlanGwName) {
		this.fromPlanGwName = fromPlanGwName;
	}

	public String getToPlanGwName() {
		return toPlanGwName;
	}

	public void setToPlanGwName(String toPlanGwName) {
		this.toPlanGwName = toPlanGwName;
	}

	public String getFromApplyGwName() {
		return fromApplyGwName;
	}

	public void setFromApplyGwName(String fromApplyGwName) {
		this.fromApplyGwName = fromApplyGwName;
	}

	public String getToApplyGwName() {
		return toApplyGwName;
	}

	public void setToApplyGwName(String toApplyGwName) {
		this.toApplyGwName = toApplyGwName;
	}

	public String getFromWriteGwName() {
		return fromWriteGwName;
	}

	public void setFromWriteGwName(String fromWriteGwName) {
		this.fromWriteGwName = fromWriteGwName;
	}

	public String getToWriteGwName() {
		return toWriteGwName;
	}

	public void setToWriteGwName(String toWriteGwName) {
		this.toWriteGwName = toWriteGwName;
	}

	public String getFromServiceGwName() {
		return fromServiceGwName;
	}

	public void setFromServiceGwName(String fromServiceGwName) {
		this.fromServiceGwName = fromServiceGwName;
	}

	public String getToServiceGwName() {
		return toServiceGwName;
	}
	public void setToServiceGwName(String toServiceGwName) {
		this.toServiceGwName = toServiceGwName;
	}

	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
}
