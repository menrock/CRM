package com.niu.crm.vo;

import java.util.List;

import com.niu.crm.model.Payment;

public class PaymentVO extends Payment{
	private static final long serialVersionUID = -5205405887251134476L;
	
	private String companyName;
	private String cstmName;
	private String creatorName;
	private String fzConfirmerName;
	private String confirmerName;
	
	private List<PaymentLineVO> lines;


	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getCstmName() {
		return cstmName;
	}
	public void setCstmName(String cstmName) {
		this.cstmName = cstmName;
	}

	public List<PaymentLineVO> getLines() {
		return lines;
	}
	public void setLines(List<PaymentLineVO> lines) {
		this.lines = lines;
	}
	
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getFzConfirmerName() {
		return fzConfirmerName;
	}
	public void setFzConfirmerName(String fzConfirmerName) {
		this.fzConfirmerName = fzConfirmerName;
	}	
	
	public String getConfirmerName() {
		return confirmerName;
	}
	public void setConfirmerName(String confirmerName) {
		this.confirmerName = confirmerName;
	}
}
