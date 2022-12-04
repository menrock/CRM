package com.niu.crm.vo;

import java.util.List;

import com.niu.crm.model.Customer;
import com.niu.crm.model.CpContactRecord;
import com.niu.crm.model.CpCustomer;
import com.niu.crm.model.StuZxgw;

public class CpCustomerVO extends CpCustomer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6256254187361152789L;
	

	private Customer customer;
	
	private String ownerName;
	private String creatorName;
	
	private List<StuZxgw> zxgwList = null;
	private List<CpContactRecord> lastContactRecords = null;
	
	public CpCustomerVO(){
		customer = new Customer();
	}
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public String getOwnerName() {
		return this.ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public String getCreatorName() {
		return this.creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public List<CpContactRecord> getLastContactRecords() {
		return lastContactRecords;
	}
	public void setLastContactRecords(List<CpContactRecord> lastContactRecords) {
		this.lastContactRecords = lastContactRecords;
	}

	public List<StuZxgw> getZxgwList() {
		return zxgwList;
	}
	public void setZxgwList(List<StuZxgw> zxgwList) {
		this.zxgwList = zxgwList;
	}
}
