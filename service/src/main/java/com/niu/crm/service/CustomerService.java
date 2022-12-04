package com.niu.crm.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.model.CustPhone;
import com.niu.crm.model.Customer;
import com.niu.crm.model.User;

public interface CustomerService {
	
	Customer load(Long id);
	
	int insert(User user, Customer customer);
        
    int update(User user, Customer customer);
    
    List<CustPhone> loadCustPhone(Long cstmId, Boolean isMain);
}
