package com.niu.crm.vo;


import java.util.List;

import com.niu.crm.model.Customer;
import com.niu.crm.model.PreContactRecord;
import com.niu.crm.model.LxPreCust;

public class LxPreCustVO extends LxPreCust{
	private static final long serialVersionUID = 5169371759587031599L;
		
	private Customer customer;
	
	private String creatorName;

	private List<PreContactRecord> lastContactRecords = null;
	
	public LxPreCustVO(){
		customer = new Customer();
	}
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public String getCreatorName() {
		return this.creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public List<PreContactRecord> getLastContactRecords() {
		return lastContactRecords;
	}
	public void setLastContactRecords(List<PreContactRecord> lastContactRecords) {
		this.lastContactRecords = lastContactRecords;
	}
}
