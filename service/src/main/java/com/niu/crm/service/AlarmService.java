package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.model.Alarm;
import com.niu.crm.form.AlarmSearchForm;

public interface AlarmService {
	
	int add(Alarm alarm);
	
	int update(Alarm alarm);
	
	int delete(Long id);
	
	Alarm load(Long id);
	
	int send(Alarm alarm);
	
	List<Alarm> queryAlarm(AlarmSearchForm form, Pageable pageable);
	
	Integer countAlarm(AlarmSearchForm form);
}
