package com.niu.crm.vo;

import java.util.List;

import com.niu.crm.model.Customer;
import com.niu.crm.model.PxContactRecord;
import com.niu.crm.model.PxCustomer;
import com.niu.crm.model.StuZxgw;

public class PxCustomerZxgwVO extends StuZxgw{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5493923291455785510L;
	
	private Customer customer;
	private PxCustomer student;
	
	private List<PxContactRecord> lastContactRecords = null;
	
	public PxCustomerZxgwVO(){
		this.setCustomer(new Customer());
		this.student  = new PxCustomer();
	}

	public List<PxContactRecord> getLastContactRecords() {
		return lastContactRecords;
	}
	public void setLastContactRecords(List<PxContactRecord> lastContactRecords) {
		this.lastContactRecords = lastContactRecords;
	}

	public PxCustomer getStudent() {
		return student;
	}
	public void setStuZxgw(PxCustomer student) {
		this.student = student;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
