package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.model.LxStuLevel;

public interface LxStuLevelMapper {
	
	int insert(LxStuLevel stuLevel);
	
	LxStuLevel selectById(Long id);

	List<LxStuLevel> selectByStuId(@Param("stuId") Long stuId);

}
