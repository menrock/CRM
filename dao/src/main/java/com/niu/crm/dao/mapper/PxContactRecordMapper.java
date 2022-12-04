package com.niu.crm.dao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.form.ContactRecordSearchForm;
import com.niu.crm.model.PxContactRecord;

import org.springframework.data.domain.Pageable;

public interface PxContactRecordMapper {
	
	int insert(PxContactRecord record);
	
	int update(PxContactRecord record);
	
	int updateAlarmed(Long id);
		
	int delete(Long id);
	
	int deleteByStuId(Long id);
	
	PxContactRecord selectById(Long id);
	
	List<PxContactRecord> selectByStuId(@Param("stuId")Long stuId, @Param("zxgwId")Long zxgwId);
	
	List<PxContactRecord> query(@Param("params")ContactRecordSearchForm form, @Param("page")Pageable page);
	
	List<PxContactRecord> queryLast(@Param("stuId")Long stuId, @Param("gwId")Long gwId, @Param("count")Integer count);
}
