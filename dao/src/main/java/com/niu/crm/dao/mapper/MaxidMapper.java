package com.niu.crm.dao.mapper;


import org.apache.ibatis.annotations.Param;

import com.niu.crm.model.Maxid;

public interface MaxidMapper {
	
	int insert(Maxid maxid);
	
	int update(Maxid maxid);
	
	Maxid select(@Param("idCode")String idCode, @Param("day")Integer day);
}
