package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.model.SysMessage;
import com.niu.crm.form.SysMessageSearchForm;

public interface SysMessageService {
	
	int add(SysMessage msg);
	
	int update(SysMessage msg);
	
	int delete(Long id);
	
	SysMessage load(Long id);
	
	List<SysMessage> queryMessage(SysMessageSearchForm form, Pageable pageable);
	
	Integer countMessage(SysMessageSearchForm form);
}
