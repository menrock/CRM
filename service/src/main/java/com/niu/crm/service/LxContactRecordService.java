package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.ContactRecordSearchForm;
import com.niu.crm.model.LxContactRecord;
import com.niu.crm.model.User;

public interface LxContactRecordService {

	LxContactRecord load(Long id);
	
	int add(User user, LxContactRecord record);
    
    void delete(User user, Long id);

    int update(User user, LxContactRecord record);
    
    int updateAlarmed(Long id);
    
    void refreshContactDate(LxContactRecord record);

    List<LxContactRecord> query(ContactRecordSearchForm form, Pageable page);
    
    List<LxContactRecord> queryLast(Long stuId, Long gwId, Integer count);
}
