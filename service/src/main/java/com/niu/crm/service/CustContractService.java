package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.model.CustContract;
import com.niu.crm.model.CustContractArchive;
import com.niu.crm.model.User;
import com.niu.crm.vo.CustContractVO;

public interface CustContractService {
	
	CustContract load(Long id);
	CustContract loadByNo(String conNo);
	
	//合同归档
	void archive(User user, CustContractArchive archive);
	
	CustContractArchive loadArchive(Long conId);
}
