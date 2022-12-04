package com.niu.crm.service;


import java.util.List;

import com.niu.crm.model.StuOperator;
import com.niu.crm.model.User;

public interface CpStuOperatorService {

	void fix(Long stuId);
	
	StuOperator load(Long id);
	
	StuOperator loadByStuIdAndOperator(Long stuId, Long operatorId);
	
    List<StuOperator> loadByStuId(Long stuId);
    
	void insert(StuOperator stuOperator);

	void update(StuOperator stuOperator);
	
	void addOperator(StuOperator stuOperator);
	
	void deleteByStuId(Long stuId);
	
	void deleteInvalid();
}
