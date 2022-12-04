package com.niu.crm.service.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.dao.mapper.CustContractArchiveMapper;
import com.niu.crm.dao.mapper.CustContractLineMapper;
import com.niu.crm.dao.mapper.CustContractMapper;
import com.niu.crm.dao.mapper.CustContractSkMapper;
import com.niu.crm.dao.mapper.MaxidMapper;
import com.niu.crm.form.ContractSearchForm;
import com.niu.crm.model.Country;
import com.niu.crm.model.CustContract;
import com.niu.crm.model.CustContractArchive;
import com.niu.crm.model.LxContractCountry;
import com.niu.crm.model.CustContractLine;
import com.niu.crm.model.Customer;
import com.niu.crm.model.Dict;
import com.niu.crm.model.LxContract;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.Maxid;
import com.niu.crm.model.ProjectContract;
import com.niu.crm.model.PxContract;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.type.BizType;
import com.niu.crm.model.type.CustContractStatus;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.CountryService;
import com.niu.crm.service.CustContractService;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.LxCustomerService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.CustContractVO;

@Service
public class CustContractServiceImpl extends BaseService implements CustContractService {
	@Autowired
	private MaxidMapper maxidMapper;
	@Autowired
	private CustContractMapper contractMapper;
	@Autowired
	private CustContractArchiveMapper contractArchiveMapper;
	@Autowired
	private CountryService countryService;
	@Autowired
	private UserService userService;
	@Autowired
	private UnitService unitService;

	@Override
	public CustContract load(Long id) {
		CustContract contract = contractMapper.selectByIdOrNo(id,null);
		return contract;
	}
	
	@Override
	public CustContract loadByNo(String conNo) {
		CustContract contract = contractMapper.selectByIdOrNo(null,conNo);
		return contract;
	}
	
	@Transactional
	@Override
	public void archive(User user, CustContractArchive archive){
		archive.setCreatorId(user.getId());
		contractArchiveMapper.insert(archive);
		contractMapper.archive(archive);
	}	
	
	@Override
	public CustContractArchive loadArchive(Long conId){
		CustContractArchive archive = contractArchiveMapper.selectByConId(conId);
		return archive;
	}	
}
