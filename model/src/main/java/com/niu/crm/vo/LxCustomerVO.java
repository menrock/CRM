package com.niu.crm.vo;

import java.util.List;

import com.niu.crm.model.Customer;
import com.niu.crm.model.LxContactRecord;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.StuZxgw;

public class LxCustomerVO extends LxCustomer{
	private static final long serialVersionUID = 5169371759587031599L;
		
	private Customer customer;
	
	private String ownerName;
	private String creatorName;
	
	private List<StuZxgw> zxgwList = null;
	private List<LxContactRecord> kfLastContactRecords = null;
	private List<LxContactRecord> gwLastContactRecords = null;
	private List<LxContactRecord> leaderLastContactRecords = null;
	private List<LxContactRecord> lastContactRecords = null;
	
	public LxCustomerVO(){
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

	public List<StuZxgw> getZxgwList() {
		return zxgwList;
	}
	public void setZxgwList(List<StuZxgw> zxgwList) {
		this.zxgwList = zxgwList;
	}

	public List<LxContactRecord> getKfLastContactRecords() {
		return kfLastContactRecords;
	}
	public void setKfLastContactRecords(List<LxContactRecord> kfLastContactRecords) {
		this.kfLastContactRecords = kfLastContactRecords;
	}

	public List<LxContactRecord> getGwLastContactRecords() {
		return gwLastContactRecords;
	}
	public void setGwLastContactRecords(List<LxContactRecord> gwLastContactRecords) {
		this.gwLastContactRecords = gwLastContactRecords;
	}

	public List<LxContactRecord> getLeaderLastContactRecords() {
		return leaderLastContactRecords;
	}
	public void setLeaderLastContactRecords(List<LxContactRecord> leaderLastContactRecords) {
		this.leaderLastContactRecords = leaderLastContactRecords;
	}

	public List<LxContactRecord> getLastContactRecords() {
		return lastContactRecords;
	}
	public void setLastContactRecords(List<LxContactRecord> lastContactRecords) {
		this.lastContactRecords = lastContactRecords;
	}
}
