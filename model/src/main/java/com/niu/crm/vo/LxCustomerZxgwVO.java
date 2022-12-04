package com.niu.crm.vo;

import java.util.List;

import com.niu.crm.model.Customer;
import com.niu.crm.model.LxContactRecord;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.StuZxgw;

public class LxCustomerZxgwVO extends StuZxgw{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1288106825081589543L;
	private Customer customer;
	private LxCustomer student;
	private String zxgwName;
	private String stuLevelName;
	
	
	private List<LxContactRecord> kfLastContactRecords = null;
	private List<LxContactRecord> gwLastContactRecords = null;
	private List<LxContactRecord> leaderLastContactRecords = null;
	private List<LxContactRecord> lastContactRecords = null;
	
	public LxCustomerZxgwVO(){
		this.setCustomer(new Customer());
		this.student  = new LxCustomer();
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
	public void setLeaderLastContactRecords(
			List<LxContactRecord> leaderLastContactRecords) {
		this.leaderLastContactRecords = leaderLastContactRecords;
	}

	public List<LxContactRecord> getLastContactRecords() {
		return lastContactRecords;
	}
	public void setLastContactRecords(List<LxContactRecord> lastContactRecords) {
		this.lastContactRecords = lastContactRecords;
	}

	public LxCustomer getStudent() {
		return student;
	}
	public void setStuZxgw(LxCustomer student) {
		this.student = student;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getZxgwName() {
		return zxgwName;
	}

	public void setZxgwName(String zxgwName) {
		this.zxgwName = zxgwName;
	}

	public String getStuLevelName() {
		return stuLevelName;
	}

	public void setStuLevelName(String stuLevelName) {
		this.stuLevelName = stuLevelName;
	}
}
