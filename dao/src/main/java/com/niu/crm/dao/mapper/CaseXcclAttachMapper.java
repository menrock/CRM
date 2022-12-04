package com.niu.crm.dao.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.CustAttachSearchForm;
import com.niu.crm.model.CaseXcclAttach;

public interface CaseXcclAttachMapper {
	
	CaseXcclAttach selectByPrimaryKey(Long id);
	
	int insert(CaseXcclAttach attach);
	
	int delete(Long id);
	
	List<CaseXcclAttach> queryAttach(@Param("params")CustAttachSearchForm searchForm, @Param("pager")Pageable pageable);
	
	Integer countAttach(@Param("params")CustAttachSearchForm searchForm);
	
}
