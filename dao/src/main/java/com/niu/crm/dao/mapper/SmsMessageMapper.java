package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.SmsSearchForm;
import com.niu.crm.model.SmsMessage;

public interface SmsMessageMapper {
	
	int insert(SmsMessage sms);
	
	int update(SmsMessage sms);
	
	List<SmsMessage> queryMessage(@Param("params") SmsSearchForm form, @Param("pager")Pageable pageable);
 
	Integer countMessage(@Param("params") SmsSearchForm form);
}
