package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.model.User;
import com.niu.crm.model.ZxgwTeam;

public interface LxZxgwTeamService {
		
	int add(ZxgwTeam zxgwTeam);
    int deleteByLeaderId(Long leaderId);

	ZxgwTeam loadByZxgwId(Long zxgwId);
    List<ZxgwTeam> loadByLeaderId(Long leaderId);
    
	
    boolean isTeamLeader(long userId);
    
    List<User> listTeamLeader();
}
