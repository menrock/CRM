package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.CollegeSearchForm;
import com.niu.crm.model.College;
import com.niu.crm.model.CoopOrg;

public interface CollegeMapper {

	College selectByPrimaryKey(@Param("id") Long id);
	
	int insert(College college);	

	int update(College college);
	
	int delete(@Param("id") Long id);
	
	int countCollege(@Param("params") CollegeSearchForm params);
	
	List<College> queryCollege(@Param("params") CollegeSearchForm params, @Param("pager")Pageable pageable);
}
