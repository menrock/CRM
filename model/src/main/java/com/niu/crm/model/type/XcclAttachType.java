package com.niu.crm.model.type;

//宣传配合材料上传
public enum XcclAttachType {
	KHGY("客户感言"), KHGXX("客户感谢信"), KHHY("与客户合影"),
	GWGY("顾问感言"), KHSP("客户视频");
	
	private String name;
	
	XcclAttachType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}