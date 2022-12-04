package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.StuShareSearchForm;
import com.niu.crm.model.StuSingleShare;

public interface StuSingleShareMapper {
	
	StuSingleShare selectById(Long id);
	
	int insert(StuSingleShare stuShare);
	
	int update(StuSingleShare stuShare);
	
	int delete(Long id);
	
	Integer countShare(@Param("params")StuShareSearchForm searchForm);

	List<StuSingleShare> queryShare(@Param("params")StuShareSearchForm searchForm, @Param("pager")Pageable pager);

}
