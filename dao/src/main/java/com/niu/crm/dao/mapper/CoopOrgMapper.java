package com.niu.crm.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.niu.crm.model.CoopOrg;

public interface CoopOrgMapper {
	
	CoopOrg selectByPrimaryKey(@Param("id") Long id);
	
	int insert(CoopOrg org);	

	int update(CoopOrg org);
	
	int delete(@Param("id") Long id);
	
	int queryCount(@Param("keywords") String keywords);
	
	List<CoopOrg> queryOrg(@Param("keywords") String keywords);
}
