package com.niu.crm.model.type;

public enum SkStatus {
	SUBMIT("未审核"), AUDIT("已审核"), REVOKEAUDIT("反审核");
	
	private String name;
	
	SkStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

}