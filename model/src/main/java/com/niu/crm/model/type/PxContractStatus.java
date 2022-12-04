package com.niu.crm.model.type;

public enum PxContractStatus {
	DRAFT("草稿"), SUBMIT("提交"), SIGNED("已签约");
	
	private String name;
	
	PxContractStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}