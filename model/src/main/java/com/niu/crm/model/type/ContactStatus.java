package com.niu.crm.model.type;

/**
 * 联系状态
 * @author 天雨
 *
 */
public enum ContactStatus {
	Y("接听"), N("未接听"), E("号码错误");
	
	private String name;
	
	ContactStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}