package com.niu.crm.dto;

import com.niu.crm.model.CpContract;
import com.niu.crm.model.CustContract;

/** 
 * @author seker(seker@ixiaopu.com)
 * @date 2017年3月30日
 */
public class CpContractDTO extends CpContract {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2980764165030628229L;
	
	private CustContract custContract;

	public CustContract getCustContract() {
		return custContract;
	}

	public void setCustContract(CustContract custContract) {
		this.custContract = custContract;
	}

}
