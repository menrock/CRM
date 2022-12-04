package com.niu.crm.vo;

import com.niu.crm.model.PaymentLine;


public class PaymentLineVO extends PaymentLine{
	private static final long serialVersionUID = -3457735398608692468L;
	
	private String conNo;
	private String itemName;
	private String companyName;

	public String getConNo() {
		return conNo;
	}
	public void setConNo(String conNo) {
		this.conNo = conNo;
	}

	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
}
