package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.model.Unit;

public interface UnitMapper {
	
	int insert(Unit unit);
	
	int update(Unit unit);
	
	int updateWxUnitId(Unit unit);
	
	int delete(Long id);
	
	Unit selectById(Long id);
	
	Unit selectByCode(String code);

	List<Unit> selectChildren(@Param("parentId") Long parentId);

}
