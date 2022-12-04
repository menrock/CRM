package com.niu.crm.model.type;

public enum CustAttachType {
	CONTRACT("合同扫描件"), BCHT("补充协议"), SHOUJU("收据"), PLAN("规划书"), RESOURCES("学生素材"),
	WSTNFB("文书头脑风暴"), WSOUTLINE("文书Outline"),
	WSFINAL("文书终稿"),  GTJL("沟通记录"), BSTC("博士套磁"), XXSQ("夏校申请"),
	DXD("院校与专业申请确认单"),
	EMAIL_PWD_EID("Email&EID&PWD"),	WSQRY("网申完成确认页"),
	END_DELAY("结案延期说明书");
	
	private String name;
	
	CustAttachType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}