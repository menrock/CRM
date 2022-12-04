package com.niu.crm.dao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.form.ContactRecordSearchForm;
import com.niu.crm.model.LxContactRecord;

import org.springframework.data.domain.Pageable;

public interface LxContactRecordMapper {
	
	int insert(LxContactRecord record);
	
	int update(LxContactRecord record);
	
	int updateAlarmed(Long id);
		
	int delete(Long id);
	
	int deleteByStuId(Long id);
	
	LxContactRecord selectById(Long id);
	
	List<LxContactRecord> selectByStuId(@Param("stuId")Long stuId, @Param("zxgwId")Long zxgwId);
	
	List<LxContactRecord> query(@Param("params")ContactRecordSearchForm form, @Param("page")Pageable page);
	
	List<LxContactRecord> queryLast(@Param("stuId")Long stuId, @Param("gwId")Long gwId, @Param("count")Integer count);
}
