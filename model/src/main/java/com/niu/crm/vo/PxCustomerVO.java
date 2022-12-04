package com.niu.crm.vo;

import java.util.List;

import com.niu.crm.model.Customer;
import com.niu.crm.model.PxContactRecord;
import com.niu.crm.model.PxCustomer;
import com.niu.crm.model.StuZxgw;

public class PxCustomerVO extends PxCustomer{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 2194846788401511821L;

	private Customer customer;
	
	private String ownerName;
	private String creatorName;
	
	private List<StuZxgw> zxgwList = null;
	private List<PxContactRecord> lastContactRecords = null;
	
	public PxCustomerVO(){
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

	public List<PxContactRecord> getLastContactRecords() {
		return lastContactRecords;
	}
	public void setLastContactRecords(List<PxContactRecord> lastContactRecords) {
		this.lastContactRecords = lastContactRecords;
	}

	public List<StuZxgw> getZxgwList() {
		return zxgwList;
	}
	public void setZxgwList(List<StuZxgw> zxgwList) {
		this.zxgwList = zxgwList;
	}
}
