package com.niu.crm.model.type;

public enum PreCustStatus {
	INIT("未处理"), INVALID("无效"), PENDING("待定"), MOVED("已转");
	
	private String name;
	
	PreCustStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

}