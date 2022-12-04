package com.niu.crm.model.type;

public enum PaymentStatus {
	DRAFT("草稿"), 
	SUBMIT("待确认"), 
	FZ_CONFIRMED("分总确认"),
	CONFIRMED("已确认");
	
	private String name;
	
	PaymentStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}