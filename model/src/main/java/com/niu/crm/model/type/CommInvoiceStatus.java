package com.niu.crm.model.type;

public enum CommInvoiceStatus {
	DRAFT("草稿"),  
	SEND("寄出"),  
	RECEIVED("到帐");
	
	private String name;
	
	CommInvoiceStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}
