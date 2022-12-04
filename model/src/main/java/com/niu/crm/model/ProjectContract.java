package com.niu.crm.model;

import java.math.BigDecimal;
import java.util.Date;


import com.niu.crm.model.type.ProjectContractStatus;

/** 
 * @author seker(seker@ixiaopu.com)
 * @date 2017年3月29日
 */
public class ProjectContract extends AbstractBizContract {
	private static final long serialVersionUID = 512113118887384818L;
	
	private Long gwId;
	private ProjectContractStatus status;
	
	public Long getGwId() {
		return gwId;
	}
	public void setGwId(Long gwId) {
		this.gwId = gwId;
	}
	
	public ProjectContractStatus getStatus() {
		return status;
	}
	public void setStatus(ProjectContractStatus status) {
		this.status = status;
	}
}
