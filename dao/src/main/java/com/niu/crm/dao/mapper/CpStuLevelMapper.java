package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.model.StuLevel;

public interface CpStuLevelMapper {
	
	int insert(StuLevel stuLevel);
	
	StuLevel selectById(Long id);

	List<StuLevel> selectByStuId(@Param("stuId") Long stuId);

}
