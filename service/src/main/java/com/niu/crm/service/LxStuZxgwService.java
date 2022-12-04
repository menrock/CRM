package com.niu.crm.service;

import java.util.List;

import com.niu.crm.model.StuZxgw;

public interface LxStuZxgwService {
	
	void add(StuZxgw stuZxgw);

	StuZxgw load(Long id);

	StuZxgw load(Long stuId, Long zxgwId);
	
	List<StuZxgw> listByStuId(Long stuId);

}
