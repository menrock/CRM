package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.model.CustContractLine;
import com.niu.crm.model.type.BizType;

public interface CustContractLineMapper {
	
	int insert(CustContractLine line);
	
	int update(CustContractLine line);
	
	int deleteByConId(@Param("conId")Long conId, @Param("bizType")BizType bizType);
	
	int delete(Long id);
	
	CustContractLine selectById(Long id);
	
	List<CustContractLine> selectByConId(@Param("conId")Long conId, @Param("bizType")BizType bizType);
}
