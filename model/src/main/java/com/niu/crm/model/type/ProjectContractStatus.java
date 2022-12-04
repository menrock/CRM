package com.niu.crm.model.type;

/**
 * 产品合同状态
 * @author seker(seker@ixiaopu.com)
 * 2017年3月29日
 */
public enum ProjectContractStatus {
	DRAFT("草稿"), SUBMIT("提交"), SIGNED("已签约");
	
	private String name;
	
	ProjectContractStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}