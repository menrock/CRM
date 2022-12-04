package com.niu.crm.service;


import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.model.Func;
import com.niu.crm.model.type.AclScope;

public interface FuncService {

    AclScope[] getAllAclScope();
    
	Func load(Long id);
	
	Func loadByCode(String code);
	
	int add(Func func);
    
    void delete(Long id);

    int update(Func func);

    List<Func> list(Pageable pager);
	
}
