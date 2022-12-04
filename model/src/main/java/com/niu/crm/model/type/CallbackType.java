package com.niu.crm.model.type;

public enum CallbackType {
	DAY1("顾问当天回访"),  
	DAY2("未接听回访"), 
	DAY3("顾问第3天回访"), 
	DAY5("组长第5天回访"), 
	DAY7("顾问第7天回访"), 
	WEEKLY("周回访"), 
	MONTHLY("月度回访"),
	FORWARD("跟进回访"),  
	KF("客服回访");
	
	private String name;
	
	CallbackType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}