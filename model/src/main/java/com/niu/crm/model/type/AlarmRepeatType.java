package com.niu.crm.model.type;

public enum AlarmRepeatType {
	ONLY("一次"), WEEKLY("每周"), MONTHLY("每月");
	
	private String name;
	
	AlarmRepeatType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

}