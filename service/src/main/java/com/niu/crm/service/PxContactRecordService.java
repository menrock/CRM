package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.ContactRecordSearchForm;
import com.niu.crm.model.PxContactRecord;
import com.niu.crm.model.User;

public interface PxContactRecordService {

	PxContactRecord load(Long id);
	
	int add(User user, PxContactRecord record);
    
    void delete(User user, Long id);

    int update(User user, PxContactRecord record);
    
    int updateAlarmed(Long id);
    
    void refreshContactDate(PxContactRecord record);

    List<PxContactRecord> query(ContactRecordSearchForm form, Pageable page);
    
    List<PxContactRecord> queryLast(Long stuId, Long gwId, Integer count);
}
