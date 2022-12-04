package com.niu.crm.service;

import java.util.List;

import com.niu.crm.model.CustContractSk;
import com.niu.crm.model.User;

public interface CustContractLineService {
	
	CustContractSk load(Long id);

	List<CustContractSk> loadByConId(Long id);
	
	int add(User user, CustContractSk contract);
    
    void delete(User user, Long id);

    int update(User user, CustContractSk contract);

    int updateSk(User user, CustContractSk contract);
}
