package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.StuShareSearchForm;
import com.niu.crm.model.StuShare;

public interface StuShareMapper {
	
	StuShare selectById(Long id);
	
	int insert(StuShare stuShare);
	
	int update(StuShare stuShare);
	
	int delete(Long id);
	
	Integer countShare(@Param("params")StuShareSearchForm searchForm);

	List<StuShare> queryShare(@Param("params")StuShareSearchForm searchForm, @Param("pager")Pageable pager);

}
