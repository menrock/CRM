package com.niu.crm.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.form.MarketStaffReportSearchForm;
import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.model.Dict;

public interface LxStatMapper {
	
	List<Map<String,Object>> statResourceByFrom(@Param("params")StudentSearchForm searchForm);

	List<Map<String,Object>> statResourceByZxgw(@Param("params")StudentSearchForm searchForm);
	
	List<Map<String,Object>> queryMarketStaffResource(@Param("params")MarketStaffReportSearchForm searchForm);
}
