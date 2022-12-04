package com.niu.crm.service;


import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.StuShareSearchForm;
import com.niu.crm.model.StuSingleShare;

public interface StuSingleShareService {
	
	StuSingleShare load(Long id);
	
	int add(StuSingleShare stuShare);
    
    int delete(Long id);

    Integer countShare(StuShareSearchForm searchForm);
    
    List<StuSingleShare> queryShare(StuShareSearchForm searchForm, Pageable pager);
}
