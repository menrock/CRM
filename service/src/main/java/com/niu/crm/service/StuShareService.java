package com.niu.crm.service;


import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.StuShareSearchForm;
import com.niu.crm.model.StuShare;

public interface StuShareService {
	
	StuShare load(Long id);
	
	int add(StuShare stuShare);
    
    int delete(Long id);

    Integer countShare(StuShareSearchForm searchForm);
    
    List<StuShare> queryShare(StuShareSearchForm searchForm, Pageable pager);
}
