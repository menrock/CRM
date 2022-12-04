package com.niu.crm.dao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.dto.CpContractDTO;
import com.niu.crm.form.ContractSearchForm;
import com.niu.crm.model.CpContract;

public interface CpContractMapper {
	
	int insert(CpContract contract);
	
	int update(CpContract contract);
	
	int updateSk(CpContract contract);
	
	int delete(Long id);
	
	CpContract selectByPrimaryKey(@Param("id")Long id);
	
	CpContract selectByConId(@Param("conId")Long conId);
	
	Date selectFirstSignDate(Long cstmId);
	
	int queryCount(@Param("params")ContractSearchForm form);
	
	List<CpContractDTO> query(@Param("params")ContractSearchForm form, @Param("pager")Pageable pageable);
	
	CpContractDTO queryStat(@Param("params")ContractSearchForm form);
}
