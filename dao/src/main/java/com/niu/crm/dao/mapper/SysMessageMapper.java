package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.SysMessageSearchForm;
import com.niu.crm.model.SysMessage;

public interface SysMessageMapper {
	
	SysMessage selectById(Long id);
	
	int insert(SysMessage msg);
	
	int update(SysMessage msg);
	
	int delete(Long id);
	
	List<SysMessage> queryMessage(@Param("params") SysMessageSearchForm form, @Param("pager")Pageable pageable);
 
	Integer countMessage(@Param("params") SysMessageSearchForm form);
}
