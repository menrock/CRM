package com.niu.crm.model.type;

public enum IDCertType {
	PASSPORT("护照"), ID("身份证");
	
	private String name;
	
	IDCertType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

}