package com.niu.crm.vo;

import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.ZxgwCallbackRemind;

public class ZxgwCallbackRemindVO extends ZxgwCallbackRemind{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1288106825081589543L;
	private StuZxgw stuZxgw;
	private LxCustomer student;
	
	private String remindTypeName;
	private String cstmName;
	
	public String getRemindTypeName() {
		return remindTypeName;
	}
	public void setRemindTypeName(String remindTypeName) {
		this.remindTypeName = remindTypeName;
	}
	
	public String getCstmName() {
		return cstmName;
	}
	public void setCstmName(String cstmName) {
		this.cstmName = cstmName;
	}

	public StuZxgw getStuZxgw() {
		return stuZxgw;
	}

	public void setStuZxgw(StuZxgw stuZxgw) {
		this.stuZxgw = stuZxgw;
	}

	public LxCustomer getStudent() {
		return student;
	}

	public void setStudent(LxCustomer student) {
		this.student = student;
	}
}
