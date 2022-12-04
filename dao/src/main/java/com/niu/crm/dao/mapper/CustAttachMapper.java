package com.niu.crm.dao.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.CustAttachSearchForm;
import com.niu.crm.model.CustAttach;

public interface CustAttachMapper {
	
	CustAttach selectByPrimaryKey(Long id);
	
	int insert(CustAttach attach);
	
	int delete(Long id);
	
	List<CustAttach> queryAttach(@Param("params")CustAttachSearchForm searchForm, @Param("pager")Pageable pageable);
	
	Integer countAttach(@Param("params")CustAttachSearchForm searchForm);
	
}
