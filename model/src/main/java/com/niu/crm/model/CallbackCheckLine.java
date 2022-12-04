package com.niu.crm.model;

import java.math.BigDecimal;

import com.niu.crm.model.type.CallbackType;

public class CallbackCheckLine {
	private Long companyId;
	private String companyName;
	
	private Long zxgwId;
	private String zxgwName;
	
	private CallbackType callbackType;
	private String callbackTypeName;
	
	private Integer totalCount;   //总数
	private Integer finishCount;  //完成数量
	private Integer ontimeCount;  //按时完成数量
	
	
	public CallbackCheckLine(){
		this.totalCount = 0;
		this.finishCount = 0;
		this.ontimeCount = 0;
	}
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Long getZxgwId() {
		return zxgwId;
	}
	public void setZxgwId(Long zxgwId) {
		this.zxgwId = zxgwId;
	}
	public String getZxgwName() {
		return zxgwName;
	}
	public void setZxgwName(String zxgwName) {
		this.zxgwName = zxgwName;
	}
	public CallbackType getCallbackType() {
		return callbackType;
	}
	public void setCallbackType(CallbackType callbackType) {
		this.callbackType = callbackType;
	}
	public String getCallbackTypeName() {
		if(callbackType == null)
			return null;
		else
			return callbackType.getName();
	}
	
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getFinishCount() {
		return finishCount;
	}
	public void setFinishCount(Integer finishCount) {
		this.finishCount = finishCount;
	}
	
	public Integer getOntimeCount() {
		return ontimeCount;
	}
	public void setOntimeCount(Integer ontimeCount) {
		this.ontimeCount = ontimeCount;
	}
	
	public String getFinishRatio(){
		if(finishCount == null || totalCount == null)
			return null;
		if(totalCount ==0)
			return "";
		
		BigDecimal dTotal = new BigDecimal(totalCount);
		BigDecimal dFinish = new BigDecimal(finishCount);
		dFinish = dFinish.multiply(new BigDecimal(100));
		
		BigDecimal ratio = dFinish.divide(dTotal, 2, BigDecimal.ROUND_HALF_UP);
		
		return ratio.toString() + "%";
		
	}
}
