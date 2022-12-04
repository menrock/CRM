package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.model.Func;

public interface FuncMapper {
	
	int insert(Func func);
	
	int update(Func func);
	
	int delete(Long id);
	
	Func selectById(Long id);
	
	Func selectByCode(String code);

	List<Func> selectFunc(@Param("pager")Pageable pager);

}
