package com.niu.crm.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.niu.crm.model.LxCustAssign;
import com.niu.crm.vo.LxCustAssignVO;
import com.niu.crm.model.User;

public interface LxCustAssignService {
	
	LxCustAssign load(Long id);
	
    List<LxCustAssignVO> selectByStuId(Long stuId);
}
