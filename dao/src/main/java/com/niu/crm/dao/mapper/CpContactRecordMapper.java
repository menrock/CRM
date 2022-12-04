package com.niu.crm.dao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.form.ContactRecordSearchForm;
import com.niu.crm.model.CpContactRecord;

import org.springframework.data.domain.Pageable;

public interface CpContactRecordMapper {
	
	int insert(CpContactRecord record);
	
	int update(CpContactRecord record);
	
	int updateAlarmed(Long id);
		
	int delete(Long id);
	
	int deleteByStuId(Long id);
	
	CpContactRecord selectById(Long id);
	
	List<CpContactRecord> selectByStuId(@Param("stuId")Long stuId, @Param("zxgwId")Long zxgwId);
	
	List<CpContactRecord> query(@Param("params")ContactRecordSearchForm form, @Param("page")Pageable page);
	
	List<CpContactRecord> queryLast(@Param("stuId")Long stuId, @Param("gwId")Long gwId, @Param("count")Integer count);
}
