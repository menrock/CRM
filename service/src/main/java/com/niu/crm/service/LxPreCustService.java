package com.niu.crm.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.niu.crm.form.PreCustSearchForm;
import com.niu.crm.model.Customer;
import com.niu.crm.model.LxPreCust;
import com.niu.crm.vo.LxPreCustVO;
import com.niu.crm.vo.RepeatCustVO;
import com.niu.crm.model.User;

public interface LxPreCustService {
	
	LxPreCust load(Long id);
	
	LxPreCustVO loadVO(Long id);
	
	LxPreCustVO loadVOByCstmId(Long cstmId);
	
	void insert(User user, Customer customer, LxPreCust lxPreCust);
	
	void updateByPrimaryKeySelective(User user, LxPreCust lxPreCust);
	
	void update(User user, Customer customer, LxPreCust lxPreCust);
	
	int delete(User user, Long id);
    
    int countPreCust(PreCustSearchForm form);
	
    List<LxPreCustVO> queryPreCust(PreCustSearchForm form, Pageable pageable);
    
    /**
     * 预咨询库 
     * @param exclueCstmId
     * @param phones
     * @return
     */
    List<RepeatCustVO> queryRepeat(Long exclueCstmId,	String... phones);
    
    //导入客户数据(返回count, msg)	
	Map<String,String> importCustomer(User user, Long unitId, Long stuFromId, MultipartFile file) throws IOException,Exception;
	
}
