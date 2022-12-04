package com.niu.crm.dao.mapper;

import com.niu.crm.model.CustContractArchive;

public interface CustContractArchiveMapper {
	
	int insert(CustContractArchive archive);
	
	int delete(Long id);
	
	CustContractArchive selectByPrimaryKey(Long id);
	
	CustContractArchive selectByConId(Long conId);
}
