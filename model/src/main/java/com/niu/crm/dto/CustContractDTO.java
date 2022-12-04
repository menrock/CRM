package com.niu.crm.dto;

import com.niu.crm.model.CustContract;
import com.niu.crm.model.LxContract;
import com.niu.crm.model.ProjectContract;
import com.niu.crm.model.PxContract;

/** 
 * @author seker(seker@ixiaopu.com)
 * @date 2017年3月29日
 */
public class CustContractDTO extends CustContract{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6153738496220693363L;
	
	private LxContract lxContract;
	private PxContract pxContract;
	private ProjectContract projectContract;
	public LxContract getLxContract() {
		return lxContract;
	}
	public void setLxContract(LxContract lxContract) {
		this.lxContract = lxContract;
	}
	public PxContract getPxContract() {
		return pxContract;
	}
	public void setPxContract(PxContract pxContract) {
		this.pxContract = pxContract;
	}
	public ProjectContract getProjectContract() {
		return projectContract;
	}
	public void setProjectContract(ProjectContract projectContract) {
		this.projectContract = projectContract;
	}
}
