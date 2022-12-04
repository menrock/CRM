package com.niu.crm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.niu.crm.model.CustContractSk;
import com.niu.crm.model.User;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.CustContractLineService;

@Service
public class CustContractLineServiceImpl extends BaseService implements CustContractLineService {

	@Override
	public CustContractSk load(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CustContractSk> loadByConId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int add(User user, CustContractSk contract) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(User user, Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public int update(User user, CustContractSk contract) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSk(User user, CustContractSk contract) {
		// TODO Auto-generated method stub
		return 0;
	}

}
