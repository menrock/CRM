package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.form.CustAssignSearchForm;
import com.niu.crm.model.CustAssign;

public interface PxCustAssignMapper {
	
	int insert(CustAssign custAssign);
	
	CustAssign selectById(Long id);

	List<CustAssign> selectByStuId(@Param("stuId") Long stuId);
	
	List<CustAssign> queryCustAssign(@Param("params") CustAssignSearchForm searchForm);
	
	int deleteById(Long id);
	
	int deleteByStuId(@Param("stuId") Long stuId);
}
