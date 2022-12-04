package com.niu.crm.model;

import java.math.BigDecimal;
import java.util.Date;

public class CustContractLine extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1530445063535167894L;
	
	private Long conId;
	private Long itemId;
	private String itemName;
	private BigDecimal itemValue;
	private BigDecimal itemDiscount;
	private BigDecimal skValue;
	private String memo;
	private Long creatorId;
	
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public BigDecimal getItemValue() {
		return itemValue;
	}
	public void setItemValue(BigDecimal itemValue) {
		this.itemValue = itemValue;
	}
	
	public BigDecimal getItemDiscount() {
		return itemDiscount;
	}
	public void setItemDiscount(BigDecimal itemDiscount) {
		this.itemDiscount = itemDiscount;
	}
	
	public BigDecimal getSkValue() {
		return skValue;
	}
	public void setSkValue(BigDecimal skValue) {
		this.skValue = skValue;
	}
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
}
