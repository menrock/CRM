package com.niu.crm.dto;

import com.niu.crm.model.ProjectContract;
import com.niu.crm.model.CustContract;

/** 
 * @author seker(seker@ixiaopu.com)
 * @date 2017年3月30日
 */
public class ProjectContractDTO extends ProjectContract {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3761984063901022163L;
	
	private CustContract custContract;

	public CustContract getCustContract() {
		return custContract;
	}

	public void setCustContract(CustContract custContract) {
		this.custContract = custContract;
	}

}
