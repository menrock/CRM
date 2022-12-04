package com.niu.crm.model.type;

public enum BizType {
	LX("留学"), PX("培训"), CP("产品");
	
	private String name;
	
	BizType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}