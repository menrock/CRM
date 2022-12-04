package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.SmsSearchForm;
import com.niu.crm.model.SmsMessage;

public interface SmsService {
	
	//取已发送条数
	String querySent();
	
	//取剩余条数
	String queryCapacity();
	
	String send(SmsMessage message);
	
	String send2Unit(String content, Long toUnitId);
	
	String send2User(String content, Long toUserId);
	
	List<SmsMessage> queryMessage(SmsSearchForm form, Pageable pageable);

	Integer countMessage(SmsSearchForm form);
}
