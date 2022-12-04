package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.PreContactRecordSearchForm;
import com.niu.crm.model.PreContactRecord;

public interface PreContactRecordMapper {
	
	int insert(PreContactRecord record);
	
	int update(PreContactRecord record);
	
	int updateAlarmed(Long id);
		
	int delete(Long id);
	
	int deleteByStuId(Long id);
	
	PreContactRecord selectByPrimaryKey(Long id);
	
	List<PreContactRecord> selectByCstmId(@Param("cstmId")Long cstmId, @Param("zxgwId")Long zxgwId);
	
	List<PreContactRecord> query(@Param("params")PreContactRecordSearchForm form, @Param("page")Pageable page);
	
	List<PreContactRecord> queryLast(@Param("cstmId")Long cstmId, @Param("gwId")Long gwId, @Param("count")Integer count);

}
