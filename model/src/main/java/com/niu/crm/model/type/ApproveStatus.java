package com.niu.crm.model.type;

public enum ApproveStatus {
	DRAFT("草稿"),SUBMIT("提交"),PASS("通过"), REJECTED("驳回");
	
	private String name;
	
	ApproveStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

}
