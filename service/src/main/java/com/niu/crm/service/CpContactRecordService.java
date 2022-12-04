package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.ContactRecordSearchForm;
import com.niu.crm.model.CpContactRecord;
import com.niu.crm.model.User;

public interface CpContactRecordService {

	CpContactRecord load(Long id);
	
	int add(User user, CpContactRecord record);
    
    void delete(User user, Long id);

    int update(User user, CpContactRecord record);
    
    int updateAlarmed(Long id);
    
    void refreshContactDate(CpContactRecord record);

    List<CpContactRecord> query(ContactRecordSearchForm form, Pageable page);
    
    List<CpContactRecord> queryLast(Long stuId, Long gwId, Integer count);
}
