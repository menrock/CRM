package com.niu.crm.dto;

import com.niu.crm.model.LxContract;
import com.niu.crm.model.CustContract;

/** 
 * @author seker(seker@ixiaopu.com)
 * @date 2017年3月30日
 */
public class LxContractDTO extends LxContract {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3761984063901022163L;	
	
	private Long stuId;
	private String conTypeName;
	private Long stuFromId;
	private String stuFromName;
	private String currSchool;
	private String stuCity;

	private String creatorName;
	private String signGwName;
	private String planGwName;
	private String applyGwName;
	private String writeGwName;
	private String serviceGwName;



	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	
	public String getStuCity() {
		return stuCity;
	}

	public void setStuCity(String stuCity) {
		this.stuCity = stuCity;
	}

	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getSignGwName() {
		return signGwName;
	}
	public void setSignGwName(String signGwName) {
		this.signGwName = signGwName;
	}

	public String getPlanGwName() {
		return planGwName;
	}
	public void setPlanGwName(String planGwName) {
		this.planGwName = planGwName;
	}

	public String getApplyGwName() {
		return applyGwName;
	}

	public void setApplyGwName(String applyGwName) {
		this.applyGwName = applyGwName;
	}

	public String getWriteGwName() {
		return writeGwName;
	}

	public void setWriteGwName(String writeGwName) {
		this.writeGwName = writeGwName;
	}

	public String getServiceGwName() {
		return serviceGwName;
	}

	public void setServiceGwName(String serviceGwName) {
		this.serviceGwName = serviceGwName;
	}

	public Long getStuFromId() {
		return stuFromId;
	}
	public void setStuFromId(Long stuFromId) {
		this.stuFromId = stuFromId;
	}

	public String getStuFromName() {
		return stuFromName;
	}
	public void setStuFromName(String stuFromName) {
		this.stuFromName = stuFromName;
	}

	public String getCurrSchool() {
		return currSchool;
	}

	public void setCurrSchool(String currSchool) {
		this.currSchool = currSchool;
	}

	public String getConTypeName() {
		return conTypeName;
	}
	public void setConTypeName(String conTypeName) {
		this.conTypeName = conTypeName;
	}
}
