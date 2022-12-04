package com.niu.crm.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;


import com.niu.crm.model.CustAssign;
import com.niu.crm.vo.PxCustAssignVO;

public interface PxCustAssignService {
	
	CustAssign load(Long id);
	
    List<PxCustAssignVO> selectByStuId(Long stuId);
}
