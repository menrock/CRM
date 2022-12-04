package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.PreContactRecordSearchForm;
import com.niu.crm.model.PreContactRecord;
import com.niu.crm.model.User;

public interface PreContactRecordService {
	PreContactRecord load(Long id);
	
	int add(User user, PreContactRecord record);
    
    void delete(User user, Long id);

    int update(User user, PreContactRecord record);
    
    int updateAlarmed(Long id);
    
    void refreshContactDate(PreContactRecord record);

    List<PreContactRecord> query(PreContactRecordSearchForm form, Pageable page);
    
    List<PreContactRecord> queryLast(Long stuId, Long gwId, Integer count);

}
