package com.niu.crm.dao.mapper;

import java.util.List;
import com.niu.crm.model.ZxgwTeam;

public interface LxZxgwTeamMapper {
	
	int insert(ZxgwTeam zxgwTeam);
	
	int deleteByLeaderId(Long leaderId);
	
	ZxgwTeam selectByZxgwId(Long zxgwId);

	List<ZxgwTeam> selectByLeaderId(Long leaderId);
	
	List<Long> selectLeaderId();
}
