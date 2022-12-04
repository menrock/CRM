package com.niu.crm.model;


import com.niu.crm.model.type.PxContractStatus;

/** 
 * @author seker(seker@ixiaopu.com)
 * @date 2017年11月20日
 */
public class CpContract extends AbstractBizContract {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -908943262363964558L;
	
	private Long gwId;
	private PxContractStatus status;
	
	public Long getGwId() {
		return gwId;
	}
	public void setGwId(Long gwId) {
		this.gwId = gwId;
	}
	
	public PxContractStatus getStatus() {
		return status;
	}
	public void setStatus(PxContractStatus status) {
		this.status = status;
	}

}
