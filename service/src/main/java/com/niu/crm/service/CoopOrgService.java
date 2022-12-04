package com.niu.crm.service;

import java.util.List;

import com.niu.crm.model.CoopOrg;
import com.niu.crm.model.User;

public interface CoopOrgService {
	CoopOrg load(Long id);
	
	int add(User user, CoopOrg org);
    
    void delete(User user, Long id);

    int update(User user, CoopOrg org);
    
    int queryCount(String keywords);
    
    List<CoopOrg> queryOrg(String keywords);
}
