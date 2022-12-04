package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.form.CustAssignSearchForm;
import com.niu.crm.model.LxCustAssign;

public interface LxCustAssignMapper {
	
	int insert(LxCustAssign custAssign);
	
	LxCustAssign selectById(Long id);

	List<LxCustAssign> selectByStuId(@Param("stuId") Long stuId);
	
	List<LxCustAssign> queryCustAssign(@Param("params") CustAssignSearchForm searchForm);
	
	int deleteById(Long id);
	
	int deleteByStuId(@Param("stuId") Long stuId);
}
