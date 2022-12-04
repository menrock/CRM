package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.AlarmSearchForm;
import com.niu.crm.model.Alarm;

public interface AlarmMapper {
	
	Alarm selectById(Long id);
	
	int insert(Alarm alarm);
	
	int update(Alarm alarm);

	int updateAfterSend(Alarm alarm);
	
	int delete(Long id);
	
	List<Alarm> queryAlarm(@Param("params") AlarmSearchForm form, @Param("pager")Pageable pageable);
 
	Integer countAlarm(@Param("params") AlarmSearchForm form);
}
