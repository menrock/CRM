package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.model.Area;

public interface AreaMapper {
	
	Area selectById(Long id);

	List<Area> selectChildren(@Param("parentId") Long parentId);

}
