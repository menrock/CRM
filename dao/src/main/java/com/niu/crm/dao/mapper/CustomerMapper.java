package com.niu.crm.dao.mapper;

import com.niu.crm.model.Customer;

public interface CustomerMapper {
	
	int insert(Customer customer);
	
	int update(Customer customer);
	
	int delete(Long id);
	
	Customer selectById(Long id);
	
	Customer selectByStuId(Long stuId);

}
