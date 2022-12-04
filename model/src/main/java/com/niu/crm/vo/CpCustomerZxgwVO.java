package com.niu.crm.vo;

import java.util.List;

import com.niu.crm.model.Customer;
import com.niu.crm.model.CpContactRecord;
import com.niu.crm.model.CpCustomer;
import com.niu.crm.model.StuZxgw;

public class CpCustomerZxgwVO extends StuZxgw{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5493923291455785510L;
	
	private Customer customer;
	private CpCustomer student;
	
	private List<CpContactRecord> lastContactRecords = null;
	
	public CpCustomerZxgwVO(){
		this.setCustomer(new Customer());
		this.student  = new CpCustomer();
	}

	public List<CpContactRecord> getLastContactRecords() {
		return lastContactRecords;
	}
	public void setLastContactRecords(List<CpContactRecord> lastContactRecords) {
		this.lastContactRecords = lastContactRecords;
	}

	public CpCustomer getStudent() {
		return student;
	}
	public void setStuZxgw(CpCustomer student) {
		this.student = student;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
