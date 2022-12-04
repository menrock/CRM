package com.niu.crm.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.model.CustContract;
import com.niu.crm.model.CustContractArchive;

public interface CustContractMapper {
	
	int insert(CustContract contract);
	
	int update(CustContract contract);
	
	int archive(CustContractArchive archive);
	
	int delete(Long id);
	
	CustContract selectByIdOrNo(@Param("id")Long id, @Param("conNo")String conNo);
	
}
