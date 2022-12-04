package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.model.StuOperator;

public interface PxStuOperatorMapper {
	
	StuOperator selectByPrimaryKey(Long id);
	
	StuOperator selectByStuIdAndOperator(@Param("stuId")Long stuId, @Param("operatorId")Long operatorId);
	
	List<StuOperator> selectByStuId(Long stuId);
	
	int insert(StuOperator record);
	
	int update(StuOperator record);
	
	void removeAclByStuId(Long stuId);
	
	void deleteByStuId(Long stuId);
	
	int deleteInvalid();
}
