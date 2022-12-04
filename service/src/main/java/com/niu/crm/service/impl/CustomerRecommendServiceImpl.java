package com.niu.crm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.dao.mapper.CustomerRecommendMapper;
import com.niu.crm.model.Customer;
import com.niu.crm.model.CustomerRecommend;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.PxCustomer;
import com.niu.crm.model.User;
import com.niu.crm.model.type.BizType;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.CpCustomerService;
import com.niu.crm.service.CustomerRecommendService;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.LxCustomerService;
import com.niu.crm.service.PxCustomerService;

@Service
public class CustomerRecommendServiceImpl extends BaseService implements
		CustomerRecommendService {
	
	@Autowired
    private CustomerRecommendMapper recommendMapper;
	
	@Autowired
    private CustomerService customerService;
	
	@Autowired
    private LxCustomerService lxService;
	
	@Autowired
    private PxCustomerService pxService;

	@Autowired
    private CpCustomerService cpService;
	
	@Transactional
	@Override
	public void lx2Px(User user, CustomerRecommend recommend) {
		Long fromBizCstmId = recommend.getFromBizCstmId();
		LxCustomer fromBizCust = lxService.load(fromBizCstmId);
		Long cstmId = fromBizCust.getCstmId();
		Customer customer = customerService.load(cstmId);
		PxCustomer toBizCust = new PxCustomer();
		
		toBizCust.setCstmId(cstmId);
		toBizCust.setCompanyId(recommend.getToCompanyId());
		toBizCust.setStuFromId(fromBizCust.getStuFromId());
		
		pxService.add(user, customer, toBizCust);
		recommend.setToBizCstmId(toBizCust.getId());
		recommend.setToBiz(BizType.PX);
	}

	@Transactional
	@Override
	public void lx2Cp(User user, CustomerRecommend recommend) {
		// TODO Auto-generated method stub

	}

	@Override
	public void px2Lx(User user, CustomerRecommend recommend) {
		// TODO Auto-generated method stub

	}

	@Transactional
	@Override
	public void px2Cp(User user, CustomerRecommend recommend) {
		// TODO Auto-generated method stub

	}

	@Transactional
	@Override
	public void cp2Lx(User user, CustomerRecommend recommend) {
		// TODO Auto-generated method stub

	}

	@Transactional
	@Override
	public void cp2Px(User user, CustomerRecommend recommend) {
		// TODO Auto-generated method stub

	}

}
