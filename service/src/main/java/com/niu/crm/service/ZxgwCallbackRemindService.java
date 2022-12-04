package com.niu.crm.service;

import java.util.List;
import java.util.Date;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.CallbackSearchForm;
import com.niu.crm.vo.LxCustomerVO;
import com.niu.crm.vo.LxCustomerZxgwVO;
import com.niu.crm.vo.ZxgwCallbackRemindVO;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.StuZxgwEx;
import com.niu.crm.model.ZxgwCallbackRemind;

public interface ZxgwCallbackRemindService {
	
	Integer countRemind(CallbackSearchForm searchForm);
	/**
	 * 查找需要提醒的资源信息
	 * @return
	 */
	List<ZxgwCallbackRemindVO> queryRemind(CallbackSearchForm searchForm, Pageable pageable);

	ZxgwCallbackRemind load(Long id);
	
	List<StuZxgwEx> getNeedRemindForWeek(Integer maxRows);
	List<StuZxgwEx> getNeedRemindForMonth(Integer maxRows);
	
	int add(ZxgwCallbackRemind record);
    
    int delete(String remindType, Date endDate);
}
