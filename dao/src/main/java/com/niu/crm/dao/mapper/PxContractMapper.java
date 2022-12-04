package com.niu.crm.dao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.dto.PxContractDTO;
import com.niu.crm.form.PxContractSearchForm;
import com.niu.crm.model.PxContract;

public interface PxContractMapper {
	
	int insert(PxContract contract);
	
	int update(PxContract contract);
	
	int updateSk(PxContract contract);
	
	int delete(Long id);
	
	PxContract selectByPrimaryKey(@Param("id")Long id);
	
	PxContract selectByConId(@Param("conId")Long conId);
	
	Date selectFirstSignDate(Long cstmId);
	
	int queryCount(@Param("params")PxContractSearchForm form);
	
	List<PxContractDTO> query(@Param("params")PxContractSearchForm form, @Param("pager")Pageable pageable);
	
	PxContractDTO queryStat(@Param("params")PxContractSearchForm form);
}
