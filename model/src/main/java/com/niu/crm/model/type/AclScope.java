package com.niu.crm.model.type;

public enum AclScope {
	SELF("本人及下属"), 
	SOMEUNIT("指定部门"), 
	SELFCOMPANY("本公司"), 
	SOMECOMPANY("指定公司"), 
	ALL("全部") ;
	
	private String name;
	
	AclScope(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}