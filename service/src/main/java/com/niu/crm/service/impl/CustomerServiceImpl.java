package com.niu.crm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.service.BaseService;
import com.niu.crm.service.CustomerService;
import com.niu.crm.dao.mapper.CustPhoneMapper;
import com.niu.crm.dao.mapper.CustomerMapper;
import com.niu.crm.model.CustPhone;
import com.niu.crm.model.Customer;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.User;

@Service
public class CustomerServiceImpl extends BaseService implements CustomerService{

    @Autowired
    private CustomerMapper  customerMapper;
    @Autowired
    private CustPhoneMapper  custPhoneMapper;
	
	public Customer load(Long id){
		return customerMapper.selectById(id);
	}
    
    /**
     * 保存前预处理
     * @param user
     * @param customer
     */
    private void preSave(User user, Customer customer){
    	if( StringUtils.isNoneEmpty(customer.getIdCertNo() ) ){
    		if(StringUtils.isEmpty(customer.getIdCertType()) )
    			customer.setIdCertType("身份证");
    	}
    	
    	List<CustPhone> custPhones = customer.getCustPhones();
    	if(custPhones == null){
    		custPhones = new ArrayList<CustPhone>();
    		customer.setCustPhones(custPhones);
    	}
    	
    	//防止丢失备用号码
    	if(custPhones.size() ==0 && StringUtils.isNotBlank(customer.getPhone()) ){
    		CustPhone custPhone = new CustPhone();
    		custPhones.add(0, custPhone);

    		custPhone.setIsMain(Boolean.FALSE);
    		custPhone.setShowPhone(customer.getPhone() );
    	}
    	
    	if(custPhones.size()==0 || !custPhones.get(0).getIsMain()){
    		CustPhone custPhone = new CustPhone();
    		custPhones.add(0, custPhone);

    		custPhone.setIsMain(Boolean.TRUE);
    		custPhone.setShowPhone(customer.getMobile() );
    	}
    	for(CustPhone custPhone:custPhones){
    		custPhone.setUpdatorId(user.getId());    		
    		String szPhone = custPhone.getShowPhone().replaceAll("-", "");
    		custPhone.setPhone(szPhone);
    	}
    }
	
	@Transactional
    @Override
	public int insert(User user, Customer customer){
    	preSave(user, customer);
    	
    	customer.setCreatorId(user.getId());    	
    	int count = customerMapper.insert(customer);
    	
    	for(CustPhone custPhone:customer.getCustPhones()){
    		custPhone.setCstmId(customer.getId());
    		custPhoneMapper.insert(custPhone);
    	}
    	
    	return count;
	}
    
    @Transactional
    @Override
	public int update(User user, Customer customer){
    	preSave(user, customer);
    	
    	custPhoneMapper.deleteByCstmId(customer.getId());
    	for(CustPhone custPhone:customer.getCustPhones()){
    		custPhone.setCstmId(customer.getId());
    		custPhoneMapper.insert(custPhone);
    	}
    	
    	return customerMapper.update(customer);
	}
    
    @Override
	public List<CustPhone> loadCustPhone(Long cstmId, Boolean isMain){
    	return custPhoneMapper.selectByCstmId(cstmId, isMain);
    }

}
